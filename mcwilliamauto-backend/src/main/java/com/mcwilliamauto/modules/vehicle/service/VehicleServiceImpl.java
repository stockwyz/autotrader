package com.mcwilliamauto.modules.vehicle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.common.PaymentCalculator;
import com.mcwilliamauto.modules.dict.entity.*;
import com.mcwilliamauto.modules.dict.mapper.*;
import com.mcwilliamauto.modules.vehicle.dto.VehicleDetailVO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleListVO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleQueryDTO;
import com.mcwilliamauto.modules.vehicle.dto.VehicleSaveDTO;
import com.mcwilliamauto.modules.vehicle.entity.Vehicle;
import com.mcwilliamauto.modules.vehicle.entity.VehicleFeature;
import com.mcwilliamauto.modules.vehicle.entity.VehicleImage;
import com.mcwilliamauto.modules.vehicle.entity.VehicleOption;
import com.mcwilliamauto.modules.vehicle.mapper.VehicleFeatureMapper;
import com.mcwilliamauto.modules.vehicle.mapper.VehicleImageMapper;
import com.mcwilliamauto.modules.vehicle.mapper.VehicleMapper;
import com.mcwilliamauto.modules.vehicle.mapper.VehicleOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleImageMapper vehicleImageMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-prefix}")
    private String accessPrefix;
    private final VehicleFeatureMapper vehicleFeatureMapper;
    private final VehicleOptionMapper vehicleOptionMapper;

    private final DictMakeMapper makeMapper;
    private final DictModelMapper modelMapper;
    private final DictBodyStyleMapper bodyStyleMapper;
    private final DictTransmissionMapper transmissionMapper;
    private final DictEngineMapper engineMapper;
    private final DictFuelTypeMapper fuelTypeMapper;
    private final DictColorMapper colorMapper;

    @Override
    public PageResult<VehicleListVO> pageList(VehicleQueryDTO dto) {
        LambdaQueryWrapper<Vehicle> wrapper = Wrappers.lambdaQuery(Vehicle.class)
                .eq(dto.getMakeId() != null, Vehicle::getMakeId, dto.getMakeId())
                .eq(dto.getModelId() != null, Vehicle::getModelId, dto.getModelId())
                .ge(dto.getMinYear() != null, Vehicle::getYear, dto.getMinYear())
                .le(dto.getMaxYear() != null, Vehicle::getYear, dto.getMaxYear())
                .eq(dto.getTransmissionId() != null, Vehicle::getTransmissionId, dto.getTransmissionId())
                .ge(dto.getMinPrice() != null, Vehicle::getPrice, dto.getMinPrice())
                .le(dto.getMaxPrice() != null, Vehicle::getPrice, dto.getMaxPrice())
                .ge(dto.getMinKm() != null, Vehicle::getOdometerKm, dto.getMinKm())
                .le(dto.getMaxKm() != null, Vehicle::getOdometerKm, dto.getMaxKm())
                .eq(dto.getBodyStyleId() != null, Vehicle::getBodyStyleId, dto.getBodyStyleId())
                .eq(dto.getExteriorColorId() != null, Vehicle::getExteriorColorId, dto.getExteriorColorId())
                .eq(dto.getEngineId() != null, Vehicle::getEngineId, dto.getEngineId())
                .eq(dto.getFuelTypeId() != null, Vehicle::getFuelTypeId, dto.getFuelTypeId())
                .eq(dto.getSold() != null, Vehicle::getIsSold, dto.getSold())
                .eq(dto.getFeatured() != null, Vehicle::getIsFeatured, dto.getFeatured())
                .eq(Vehicle::getStatus, 1)
                .and(StringUtils.hasText(dto.getKeyword()), w -> w
                        .like(Vehicle::getTitle, dto.getKeyword())
                        .or().like(Vehicle::getStockNo, dto.getKeyword())
                        .or().like(Vehicle::getVin, dto.getKeyword()));

        applySort(wrapper, dto.getSortBy(), dto.getSortOrder());

        Page<Vehicle> page = vehicleMapper.selectPage(
                new Page<>(dto.getPageNum(), dto.getPageSize()), wrapper);

        List<VehicleListVO> voList = toListVO(page.getRecords());

        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    private void applySort(LambdaQueryWrapper<Vehicle> wrapper, String sortBy, String sortOrder) {
        boolean asc = !"desc".equalsIgnoreCase(sortOrder);
        if (sortBy == null) sortBy = "createdAt";
        switch (sortBy) {
            case "price" -> wrapper.orderBy(true, asc, Vehicle::getPrice);
            case "year" -> wrapper.orderBy(true, asc, Vehicle::getYear);
            case "mileage" -> wrapper.orderBy(true, asc, Vehicle::getOdometerKm);
            case "make" -> wrapper.orderBy(true, asc, Vehicle::getMakeId);
            case "model" -> wrapper.orderBy(true, asc, Vehicle::getModelId);
            case "bodyStyle" -> wrapper.orderBy(true, asc, Vehicle::getBodyStyleId);
            default -> wrapper.orderByDesc(Vehicle::getCreatedAt);
        }
    }

    private List<VehicleListVO> toListVO(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) return Collections.emptyList();

        Set<Long> makeIds = vehicles.stream().map(Vehicle::getMakeId).collect(Collectors.toSet());
        Set<Long> modelIds = vehicles.stream().map(Vehicle::getModelId).collect(Collectors.toSet());
        Set<Long> bodyStyleIds = vehicles.stream().map(Vehicle::getBodyStyleId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> transmissionIds = vehicles.stream().map(Vehicle::getTransmissionId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> engineIds = vehicles.stream().map(Vehicle::getEngineId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> fuelTypeIds = vehicles.stream().map(Vehicle::getFuelTypeId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> colorIds = new HashSet<>();
        vehicles.forEach(v -> {
            if (v.getExteriorColorId() != null) colorIds.add(v.getExteriorColorId());
            if (v.getInteriorColorId() != null) colorIds.add(v.getInteriorColorId());
        });
        Set<Long> vehicleIds = vehicles.stream().map(Vehicle::getId).collect(Collectors.toSet());

        Map<Long, String> makeMap = batchNameMap(makeMapper.selectBatchIds(makeIds), DictMake::getId, DictMake::getName);
        Map<Long, String> modelMap = batchNameMap(modelMapper.selectBatchIds(modelIds), DictModel::getId, DictModel::getName);
        Map<Long, String> bodyStyleMap = bodyStyleIds.isEmpty() ? Collections.emptyMap() :
                batchNameMap(bodyStyleMapper.selectBatchIds(bodyStyleIds), DictBodyStyle::getId, DictBodyStyle::getName);
        Map<Long, String> transmissionMap = transmissionIds.isEmpty() ? Collections.emptyMap() :
                batchNameMap(transmissionMapper.selectBatchIds(transmissionIds), DictTransmission::getId, DictTransmission::getName);
        Map<Long, String> engineMap = engineIds.isEmpty() ? Collections.emptyMap() :
                batchNameMap(engineMapper.selectBatchIds(engineIds), DictEngine::getId, DictEngine::getName);
        Map<Long, String> fuelTypeMap = fuelTypeIds.isEmpty() ? Collections.emptyMap() :
                batchNameMap(fuelTypeMapper.selectBatchIds(fuelTypeIds), DictFuelType::getId, DictFuelType::getName);
        Map<Long, String> colorMap = colorIds.isEmpty() ? Collections.emptyMap() :
                batchNameMap(colorMapper.selectBatchIds(colorIds), DictColor::getId, DictColor::getName);

        List<VehicleImage> mainImages = vehicleImageMapper.selectList(
                Wrappers.<VehicleImage>lambdaQuery()
                        .in(VehicleImage::getVehicleId, vehicleIds)
                        .eq(VehicleImage::getIsMain, true));
        Map<Long, VehicleImage> mainImageMap = mainImages.stream()
                .collect(Collectors.toMap(VehicleImage::getVehicleId, m -> m, (a, b) -> a));

        return vehicles.stream().map(v -> {
            VehicleListVO vo = new VehicleListVO();
            vo.setId(v.getId());
            vo.setTitle(v.getTitle());
            vo.setSlug(v.getSlug());
            vo.setStockNo(v.getStockNo());
            vo.setVin(v.getVin());
            vo.setYear(v.getYear());
            vo.setMakeName(makeMap.get(v.getMakeId()));
            vo.setModelName(modelMap.get(v.getModelId()));
            vo.setBodyStyleName(bodyStyleMap.get(v.getBodyStyleId()));
            vo.setTransmissionName(transmissionMap.get(v.getTransmissionId()));
            vo.setEngineName(engineMap.get(v.getEngineId()));
            vo.setFuelTypeName(fuelTypeMap.get(v.getFuelTypeId()));
            vo.setDriveline(v.getDriveline());
            vo.setExteriorColorName(colorMap.get(v.getExteriorColorId()));
            vo.setInteriorColorName(colorMap.get(v.getInteriorColorId()));
            vo.setDoors(v.getDoors());
            vo.setOdometerKm(v.getOdometerKm());
            vo.setPrice(v.getPrice());
            vo.setBiweeklyPayment(PaymentCalculator.calcBiweekly(v.getPrice(), v.getLoanTermMonths()));
            vo.setLoanTermMonths(v.getLoanTermMonths() == null ? PaymentCalculator.DEFAULT_TERM_MONTHS : v.getLoanTermMonths());
            VehicleImage img = mainImageMap.get(v.getId());
            vo.setMainImageUrl(img == null ? null : img.getImageUrl());
            vo.setMainThumbUrl(img == null ? null : img.getThumbUrl());
            vo.setIsOneOwner(v.getIsOneOwner());
            vo.setIsLowKm(v.getIsLowKm());
            vo.setIsFeatured(v.getIsFeatured());
            vo.setIsSold(v.getIsSold());
            return vo;
        }).collect(Collectors.toList());
    }

    private <T> Map<Long, String> batchNameMap(List<T> list, java.util.function.Function<T, Long> idFn,
                                                java.util.function.Function<T, String> nameFn) {
        Map<Long, String> map = new HashMap<>();
        for (T t : list) map.put(idFn.apply(t), nameFn.apply(t));
        return map;
    }

    @Override
    public VehicleDetailVO getDetail(Long id) {
        Vehicle v = vehicleMapper.selectById(id);
        if (v == null) throw new BusinessException(404, "Vehicle not found");
        vehicleMapper.update(null, Wrappers.<Vehicle>lambdaUpdate()
                .eq(Vehicle::getId, id)
                .setSql("view_count = view_count + 1"));

        VehicleDetailVO vo = new VehicleDetailVO();
        vo.setId(v.getId());
        vo.setStockNo(v.getStockNo());
        vo.setVin(v.getVin());
        vo.setTitle(v.getTitle());
        vo.setSlug(v.getSlug());
        vo.setMakeName(nameOrNull(makeMapper.selectById(v.getMakeId()), DictMake::getName));
        vo.setModelName(nameOrNull(modelMapper.selectById(v.getModelId()), DictModel::getName));
        vo.setYear(v.getYear());
        vo.setBodyStyleName(nameOrNull(bodyStyleMapper.selectById(v.getBodyStyleId()), DictBodyStyle::getName));
        vo.setTransmissionName(nameOrNull(transmissionMapper.selectById(v.getTransmissionId()), DictTransmission::getName));
        vo.setEngineName(nameOrNull(engineMapper.selectById(v.getEngineId()), DictEngine::getName));
        vo.setEngineSize(v.getEngineSize());
        vo.setFuelTypeName(nameOrNull(fuelTypeMapper.selectById(v.getFuelTypeId()), DictFuelType::getName));
        vo.setDriveline(v.getDriveline());
        vo.setExteriorColorName(nameOrNull(colorMapper.selectById(v.getExteriorColorId()), DictColor::getName));
        vo.setInteriorColorName(nameOrNull(colorMapper.selectById(v.getInteriorColorId()), DictColor::getName));
        vo.setDoors(v.getDoors());
        vo.setPassengers(v.getPassengers());

        // 编辑表单下拉框回显需要的原始ID
        vo.setMakeId(v.getMakeId());
        vo.setModelId(v.getModelId());
        vo.setBodyStyleId(v.getBodyStyleId());
        vo.setTransmissionId(v.getTransmissionId());
        vo.setEngineId(v.getEngineId());
        vo.setFuelTypeId(v.getFuelTypeId());
        vo.setExteriorColorId(v.getExteriorColorId());
        vo.setInteriorColorId(v.getInteriorColorId());
        vo.setStatus(v.getStatus());

        vo.setOdometerKm(v.getOdometerKm());
        vo.setCityFuelConsumption(v.getCityFuelConsumption());
        vo.setHwyFuelConsumption(v.getHwyFuelConsumption());
        vo.setPrice(v.getPrice());
        vo.setBiweeklyPayment(PaymentCalculator.calcBiweekly(v.getPrice(), v.getLoanTermMonths()));
        vo.setLoanTermMonths(v.getLoanTermMonths() == null ? PaymentCalculator.DEFAULT_TERM_MONTHS : v.getLoanTermMonths());
        vo.setDescription(v.getDescription());
        vo.setIsOneOwner(v.getIsOneOwner());
        vo.setIsLowKm(v.getIsLowKm());
        vo.setIsFeatured(v.getIsFeatured());
        vo.setIsSold(v.getIsSold());
        vo.setCarfaxPdfUrl(v.getCarfaxPdfUrl());
        vo.setDealerPermitNo(v.getDealerPermitNo());
        vo.setVideoUrl(v.getVideoUrl());

        vo.setImages(vehicleImageMapper.selectList(Wrappers.<VehicleImage>lambdaQuery()
                .eq(VehicleImage::getVehicleId, id).orderByAsc(VehicleImage::getSortNo)));
        vo.setFeatures(vehicleFeatureMapper.selectList(Wrappers.<VehicleFeature>lambdaQuery()
                .eq(VehicleFeature::getVehicleId, id).orderByAsc(VehicleFeature::getCategory).orderByAsc(VehicleFeature::getSortNo)));
        vo.setOptions(vehicleOptionMapper.selectList(Wrappers.<VehicleOption>lambdaQuery()
                .eq(VehicleOption::getVehicleId, id).orderByAsc(VehicleOption::getOptionGroup).orderByAsc(VehicleOption::getSortNo)));

        Vehicle prev = vehicleMapper.selectOne(Wrappers.<Vehicle>lambdaQuery()
                .lt(Vehicle::getId, id).eq(Vehicle::getStatus, 1)
                .orderByDesc(Vehicle::getId).last("limit 1"));
        Vehicle next = vehicleMapper.selectOne(Wrappers.<Vehicle>lambdaQuery()
                .gt(Vehicle::getId, id).eq(Vehicle::getStatus, 1)
                .orderByAsc(Vehicle::getId).last("limit 1"));
        vo.setPrevVehicleId(prev == null ? null : prev.getId());
        vo.setNextVehicleId(next == null ? null : next.getId());

        return vo;
    }

    private <T> String nameOrNull(T entity, java.util.function.Function<T, String> nameFn) {
        return entity == null ? null : nameFn.apply(entity);
    }

    @Override
    @Transactional
    public Long saveVehicle(VehicleSaveDTO dto) {
        Vehicle v = new Vehicle();
        org.springframework.beans.BeanUtils.copyProperties(dto, v, "features", "options");
        if (v.getSlug() == null || v.getSlug().isBlank()) {
            v.setSlug(buildSlug(dto));
        }
        if (dto.getId() == null) {
            v.setStockNo(generateStockNo()); // 新增车辆时忽略前端传入的库存编号，服务端自动生成
            v.setViewCount(0);
            v.setSortNo(0);
            vehicleMapper.insert(v);
        } else {
            v.setId(dto.getId());
            v.setStockNo(null); // 编辑时不允许修改库存编号，置null配合下方逐列update时跳过该字段
            vehicleMapper.updateById(v);
            vehicleFeatureMapper.delete(Wrappers.<VehicleFeature>lambdaQuery().eq(VehicleFeature::getVehicleId, v.getId()));
            vehicleOptionMapper.delete(Wrappers.<VehicleOption>lambdaQuery().eq(VehicleOption::getVehicleId, v.getId()));
        }

        Long vehicleId = v.getId();
        if (dto.getFeatures() != null) {
            int sort = 0;
            for (VehicleSaveDTO.FeatureItem item : dto.getFeatures()) {
                VehicleFeature f = new VehicleFeature();
                f.setVehicleId(vehicleId);
                f.setCategory(item.getCategory());
                f.setContent(item.getContent());
                f.setSortNo(sort++);
                vehicleFeatureMapper.insert(f);
            }
        }
        if (dto.getOptions() != null) {
            int sort = 0;
            for (VehicleSaveDTO.OptionItem item : dto.getOptions()) {
                VehicleOption o = new VehicleOption();
                o.setVehicleId(vehicleId);
                o.setOptionGroup(item.getOptionGroup());
                o.setContent(item.getContent());
                o.setSortNo(sort++);
                vehicleOptionMapper.insert(o);
            }
        }
        return vehicleId;
    }

    private String buildSlug(VehicleSaveDTO dto) {
        String base = dto.getYear() + "-" + dto.getTitle().replaceAll("[^a-zA-Z0-9]+", "-");
        return base + "-" + System.currentTimeMillis() % 1000000;
    }

    /**
     * 生成库存编号：当前年份(4位) + 5位顺序码，如 202600001。
     * 顺序码按"当年最大值+1"计算，年份变化时因为查不到当年的历史记录，自动从00001重新开始。
     */
    private String generateStockNo() {
        String year = String.valueOf(java.time.LocalDate.now().getYear());
        Vehicle last = vehicleMapper.selectOne(Wrappers.<Vehicle>lambdaQuery()
                .likeRight(Vehicle::getStockNo, year)
                .orderByDesc(Vehicle::getStockNo)
                .last("limit 1"));
        int nextSeq = 1;
        if (last != null && last.getStockNo() != null && last.getStockNo().length() == 9) {
            try {
                nextSeq = Integer.parseInt(last.getStockNo().substring(4)) + 1;
            } catch (NumberFormatException ignored) {
                // 万一历史数据格式不对，就从1开始，不影响新增流程
            }
        }
        return year + String.format("%05d", nextSeq);
    }

    @Override
    public String getStockNo(Long vehicleId) {
        Vehicle v = vehicleMapper.selectById(vehicleId);
        return v == null ? null : v.getStockNo();
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        vehicleMapper.deleteById(id);
        vehicleImageMapper.delete(Wrappers.<VehicleImage>lambdaQuery().eq(VehicleImage::getVehicleId, id));
        vehicleFeatureMapper.delete(Wrappers.<VehicleFeature>lambdaQuery().eq(VehicleFeature::getVehicleId, id));
        vehicleOptionMapper.delete(Wrappers.<VehicleOption>lambdaQuery().eq(VehicleOption::getVehicleId, id));
    }

    @Override
    public void updateStatus(Long id, String field, Boolean value) {
        LambdaQueryWrapper<Vehicle> check = Wrappers.<Vehicle>lambdaQuery().eq(Vehicle::getId, id);
        if (vehicleMapper.selectCount(check) == 0) throw new BusinessException(404, "Vehicle not found");

        Vehicle update = new Vehicle();
        update.setId(id);
        switch (field) {
            case "sold" -> {
                update.setIsSold(value);
                if (Boolean.TRUE.equals(value)) update.setSoldAt(LocalDateTime.now());
            }
            case "featured" -> update.setIsFeatured(value);
            case "status" -> update.setStatus(Boolean.TRUE.equals(value) ? 1 : 0);
            default -> throw new BusinessException("Unsupported field: " + field);
        }
        vehicleMapper.updateById(update);
    }

    @Override
    public List<VehicleImage> attachImages(Long vehicleId, List<String[]> urlThumbPairs) {
        List<VehicleImage> existing = vehicleImageMapper.selectList(
                Wrappers.<VehicleImage>lambdaQuery().eq(VehicleImage::getVehicleId, vehicleId));
        // 从当前已有图片里最大的sortNo接着往后排，不再固定从0开始，避免新加的图片跟已有图片sortNo撞车
        int maxSort = existing.stream().mapToInt(i -> i.getSortNo() == null ? 0 : i.getSortNo()).max().orElse(-1) + 1;
        boolean hasMain = existing.stream().anyMatch(VehicleImage::getIsMain);
        List<VehicleImage> created = new java.util.ArrayList<>();
        for (String[] pair : urlThumbPairs) {
            VehicleImage img = new VehicleImage();
            img.setVehicleId(vehicleId);
            img.setImageUrl(pair[0]);
            img.setThumbUrl(pair[1]);
            img.setSortNo(maxSort++);
            img.setIsMain(!hasMain);
            hasMain = true;
            vehicleImageMapper.insert(img); // MyBatis-Plus插入后会把数据库自增生成的id自动回填到img.id上
            created.add(img);
        }
        return created;
    }

    @Override
    public void deleteImage(Long imageId) {
        VehicleImage img = vehicleImageMapper.selectById(imageId);
        if (img == null) throw new BusinessException(404, "Image not found");

        // 先删物理文件（原图+缩略图），失败就中止，不会继续删数据库记录，
        // 跟品牌Logo那边定下的"先删文件、失败即中止"是同一套逻辑
        deleteVehicleFileStrict(img.getImageUrl());
        deleteVehicleFileStrict(img.getThumbUrl());

        boolean wasMain = Boolean.TRUE.equals(img.getIsMain());
        Long vehicleId = img.getVehicleId();
        vehicleImageMapper.deleteById(imageId);

        // 如果删掉的正好是主图，随便挑一张剩下的图顶上，保证列表页缩略图不会突然消失
        if (wasMain) {
            VehicleImage next = vehicleImageMapper.selectOne(Wrappers.<VehicleImage>lambdaQuery()
                    .eq(VehicleImage::getVehicleId, vehicleId).orderByAsc(VehicleImage::getSortNo).last("limit 1"));
            if (next != null) {
                VehicleImage update = new VehicleImage();
                update.setId(next.getId());
                update.setIsMain(true);
                vehicleImageMapper.updateById(update);
            }
        }
    }

    @Override
    public void reorderImages(Long vehicleId, List<Long> orderedImageIds) {
        // 防止越权：先查出这台车真实拥有的图片ID集合，过滤掉不属于这台车的id，避免恶意传参改到别的车
        Set<Long> ownedIds = vehicleImageMapper.selectList(
                        Wrappers.<VehicleImage>lambdaQuery().eq(VehicleImage::getVehicleId, vehicleId))
                .stream().map(VehicleImage::getId).collect(Collectors.toSet());

        int sort = 0;
        for (Long imageId : orderedImageIds) {
            if (!ownedIds.contains(imageId)) continue;
            VehicleImage update = new VehicleImage();
            update.setId(imageId);
            update.setSortNo(sort);
            update.setIsMain(sort == 0); // 排在第一位的自动成为主图/封面图
            vehicleImageMapper.updateById(update);
            sort++;
        }
    }

    /** 严格版：URL存的是带 accessPrefix 的完整相对链接(如 /uploads/202600001/pic-xxx.jpg)，
     *  先去掉 accessPrefix 前缀换算成物理路径，文件不存在就跳过，删除失败就抛异常中止 */
    private void deleteVehicleFileStrict(String urlWithPrefix) {
        if (!StringUtils.hasText(urlWithPrefix)) return;
        String relativePath = urlWithPrefix.startsWith(accessPrefix + "/")
                ? urlWithPrefix.substring((accessPrefix + "/").length())
                : urlWithPrefix;
        File target = new File(uploadDir, relativePath);
        if (!target.exists() || !target.isFile()) return;
        try {
            java.nio.file.Files.delete(target.toPath());
        } catch (java.io.IOException e) {
            throw new BusinessException(
                    "Could not delete the image file (" + relativePath + "): " + e.getMessage()
                            + ". It may be in use by another program. Please try again.");
        }
    }

    @Override
    public void deleteVideo(Long vehicleId) {
        Vehicle v = vehicleMapper.selectById(vehicleId);
        if (v == null) throw new BusinessException(404, "Vehicle not found");
        deleteVehicleFileStrict(v.getVideoUrl());
        vehicleMapper.update(null, Wrappers.<Vehicle>lambdaUpdate()
                .eq(Vehicle::getId, vehicleId)
                .set(Vehicle::getVideoUrl, null));
    }

    @Override
    public void deleteCarfax(Long vehicleId) {
        Vehicle v = vehicleMapper.selectById(vehicleId);
        if (v == null) throw new BusinessException(404, "Vehicle not found");
        deleteVehicleFileStrict(v.getCarfaxPdfUrl());
        vehicleMapper.update(null, Wrappers.<Vehicle>lambdaUpdate()
                .eq(Vehicle::getId, vehicleId)
                .set(Vehicle::getCarfaxPdfUrl, null));
    }

    /** 上传新视频替换旧视频时调用：只删旧的物理文件，不碰数据库(新videoUrl由前端后续的保存请求负责写入)。
     *  查不到车/原本没有视频就直接跳过，不算错误。 */
    @Override
    public void deleteOldVideoFile(Long vehicleId) {
        Vehicle v = vehicleMapper.selectById(vehicleId);
        if (v == null) return;
        deleteVehicleFileStrict(v.getVideoUrl());
    }
}
