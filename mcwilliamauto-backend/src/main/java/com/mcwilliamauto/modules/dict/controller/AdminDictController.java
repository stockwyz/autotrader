package com.mcwilliamauto.modules.dict.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.dict.entity.*;
import com.mcwilliamauto.modules.dict.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * 后台字典管理：品牌/车型/车身类型/变速箱/发动机/燃料类型/颜色 的增删改查，需登录。
 * 字典表结构简单，直接用 BaseMapper 自带的CRUD方法，不额外拆分Service层。
 */
@RestController
@RequestMapping("/api/admin/dict")
@RequiredArgsConstructor
public class AdminDictController {

    private final DictMakeMapper makeMapper;
    private final DictModelMapper modelMapper;
    private final DictBodyStyleMapper bodyStyleMapper;
    private final DictTransmissionMapper transmissionMapper;
    private final DictEngineMapper engineMapper;
    private final DictFuelTypeMapper fuelTypeMapper;
    private final DictColorMapper colorMapper;
    private final ProLabelMapper proLabelMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ---------- 品牌 Make ----------
    @GetMapping("/makes")
    public Result<?> makeList(@RequestParam(defaultValue = "1") Long pageNum,
                               @RequestParam(defaultValue = "20") Long pageSize) {
        Page<DictMake> page = makeMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<DictMake>lambdaQuery().orderByAsc(DictMake::getSortNo));
        page.getRecords().forEach(m -> {
            if (StringUtils.hasText(m.getLogoUrl()) && !m.getLogoUrl().startsWith("http")) {
                m.setLogoUrl(baseUrl + accessPrefix + "/" + m.getLogoUrl());
            }
        });
        return Result.success(page);
    }

    @PostMapping("/makes")
    public Result<?> makeCreate(@RequestBody DictMake entity) {
        entity.setLogoUrl(normalizeLogoUrl(entity.getLogoUrl()));
        makeMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/makes/{id}")
    public Result<?> makeUpdate(@PathVariable Long id, @RequestBody DictMake entity) {
        entity.setLogoUrl(normalizeLogoUrl(entity.getLogoUrl()));

        DictMake old = makeMapper.selectById(id);
        String oldLogoUrl = old == null ? null : old.getLogoUrl();

        // Logo有变化时（换了新图，或者清空了），先物理删除旧文件；
        // 删除失败直接抛异常中止，不会继续往下更新数据库，
        // 避免"文件删不掉、数据库却已经指向新图"这种不一致状态
        if (StringUtils.hasText(oldLogoUrl) && !oldLogoUrl.equals(entity.getLogoUrl())) {
            deleteLogoFileStrict(oldLogoUrl);
        }

        // 用显式 UpdateWrapper.set(...) 逐列指定要更新的值：
        // 这种写法不会像 updateById(entity) 那样自动跳过null字段，
        // 不管 logoUrl 是不是null，都会真正把这一列写进数据库，
        // 从根上避免依赖 FieldStrategy 枚举（不同MyBatis-Plus版本这个API不太稳定）
        makeMapper.update(null, Wrappers.<DictMake>lambdaUpdate()
                .eq(DictMake::getId, id)
                .set(DictMake::getName, entity.getName())
                .set(DictMake::getLogoUrl, entity.getLogoUrl())
                .set(DictMake::getSortNo, entity.getSortNo())
                .set(DictMake::getStatus, entity.getStatus()));

        return Result.success();
    }

    /** 严格版：先判断文件是否存在，存在才删；删除失败直接抛异常中止后续操作（用于"用户明确要删除这个Logo/记录"的场景） */
    private void deleteLogoFileStrict(String relativePath) {
        File target = new File(uploadDir, relativePath);
        if (!target.exists() || !target.isFile()) {
            return; // 文件本来就不存在，不算错误，直接跳过
        }
        try {
            java.nio.file.Files.delete(target.toPath());
        } catch (java.io.IOException e) {
            throw new BusinessException(
                    "Could not delete the old logo file (" + relativePath + "): " + e.getMessage()
                            + ". It may be in use by another program. Please try again.");
        }
    }

    /** 不管前端传入的是完整http链接还是上传接口直接返回的相对链接(/uploads/x)，
     *  只要包含 accessPrefix 就统一去掉，只留纯相对路径再入库，避免读取时被重复拼接前缀 */
    private String normalizeLogoUrl(String url) {
        if (!StringUtils.hasText(url)) return url;
        String marker = accessPrefix + "/";
        int idx = url.indexOf(marker);
        if (idx >= 0) {
            return url.substring(idx + marker.length());
        }
        return url;
    }

    @DeleteMapping("/makes/{id}")
    public Result<?> makeDelete(@PathVariable Long id) {
        // 先删物理文件：如果文件删除失败（比如被占用），直接抛异常中止，
        // 不会继续往下执行数据库删除，避免出现"文件删不掉、数据库记录却没了"这种不一致状态
        DictMake target = makeMapper.selectById(id);
        if (target != null && StringUtils.hasText(target.getLogoUrl())) {
            deleteLogoFileStrict(target.getLogoUrl());
        }
        makeMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 车型 Model ----------
    @GetMapping("/models")
    public Result<?> modelList(@RequestParam(required = false) Long makeId,
                                @RequestParam(defaultValue = "1") Long pageNum,
                                @RequestParam(defaultValue = "20") Long pageSize) {
        Page<DictModel> page = modelMapper.selectPageWithMake(new Page<>(pageNum, pageSize), makeId);
        page.getRecords().forEach(model -> {
            String logo = model.getMakeLogoUrl();
            if (StringUtils.hasText(logo) && !logo.startsWith("http")) {
                model.setMakeLogoUrl(baseUrl + accessPrefix + "/" + logo);
            }
        });
        return Result.success(page);
    }

    @PostMapping("/models")
    public Result<?> modelCreate(@RequestBody DictModel entity) {
        modelMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/models/{id}")
    public Result<?> modelUpdate(@PathVariable Long id, @RequestBody DictModel entity) {
        entity.setId(id);
        modelMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/models/{id}")
    public Result<?> modelDelete(@PathVariable Long id) {
        modelMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 车身类型 BodyStyle ----------
    @GetMapping("/body-styles")
    public Result<?> bodyStyleList(@RequestParam(defaultValue = "1") Long pageNum,
                                    @RequestParam(defaultValue = "20") Long pageSize) {
        Page<DictBodyStyle> page = bodyStyleMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<DictBodyStyle>lambdaQuery().orderByAsc(DictBodyStyle::getSortNo));
        page.getRecords().forEach(b -> {
            if (StringUtils.hasText(b.getPhotoUrl()) && !b.getPhotoUrl().startsWith("http")) {
                b.setPhotoUrl(baseUrl + accessPrefix + "/" + b.getPhotoUrl());
            }
        });
        return Result.success(page);
    }

    @PostMapping("/body-styles")
    public Result<?> bodyStyleCreate(@RequestBody DictBodyStyle entity) {
        entity.setPhotoUrl(normalizeLogoUrl(entity.getPhotoUrl()));
        bodyStyleMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/body-styles/{id}")
    public Result<?> bodyStyleUpdate(@PathVariable Long id, @RequestBody DictBodyStyle entity) {
        entity.setPhotoUrl(normalizeLogoUrl(entity.getPhotoUrl()));

        DictBodyStyle old = bodyStyleMapper.selectById(id);
        String oldPhotoUrl = old == null ? null : old.getPhotoUrl();
        if (StringUtils.hasText(oldPhotoUrl) && !oldPhotoUrl.equals(entity.getPhotoUrl())) {
            deleteLogoFileStrict(oldPhotoUrl);
        }

        bodyStyleMapper.update(null, Wrappers.<DictBodyStyle>lambdaUpdate()
                .eq(DictBodyStyle::getId, id)
                .set(DictBodyStyle::getName, entity.getName())
                .set(DictBodyStyle::getPhotoUrl, entity.getPhotoUrl())
                .set(DictBodyStyle::getSortNo, entity.getSortNo())
                .set(DictBodyStyle::getStatus, entity.getStatus()));
        return Result.success();
    }

    @DeleteMapping("/body-styles/{id}")
    public Result<?> bodyStyleDelete(@PathVariable Long id) {
        DictBodyStyle target = bodyStyleMapper.selectById(id);
        if (target != null && StringUtils.hasText(target.getPhotoUrl())) {
            deleteLogoFileStrict(target.getPhotoUrl());
        }
        bodyStyleMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 变速箱 Transmission ----------
    @GetMapping("/transmissions")
    public Result<?> transmissionList(@RequestParam(defaultValue = "1") Long pageNum,
                                       @RequestParam(defaultValue = "20") Long pageSize) {
        return Result.success(transmissionMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.emptyWrapper()));
    }

    @PostMapping("/transmissions")
    public Result<?> transmissionCreate(@RequestBody DictTransmission entity) {
        transmissionMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/transmissions/{id}")
    public Result<?> transmissionUpdate(@PathVariable Long id, @RequestBody DictTransmission entity) {
        entity.setId(id);
        transmissionMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/transmissions/{id}")
    public Result<?> transmissionDelete(@PathVariable Long id) {
        transmissionMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 发动机 Engine ----------
    @GetMapping("/engines")
    public Result<?> engineList(@RequestParam(defaultValue = "1") Long pageNum,
                                 @RequestParam(defaultValue = "20") Long pageSize) {
        return Result.success(engineMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.emptyWrapper()));
    }

    @PostMapping("/engines")
    public Result<?> engineCreate(@RequestBody DictEngine entity) {
        engineMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/engines/{id}")
    public Result<?> engineUpdate(@PathVariable Long id, @RequestBody DictEngine entity) {
        entity.setId(id);
        engineMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/engines/{id}")
    public Result<?> engineDelete(@PathVariable Long id) {
        engineMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 燃料类型 FuelType ----------
    @GetMapping("/fuel-types")
    public Result<?> fuelTypeList(@RequestParam(defaultValue = "1") Long pageNum,
                                   @RequestParam(defaultValue = "20") Long pageSize) {
        return Result.success(fuelTypeMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.emptyWrapper()));
    }

    @PostMapping("/fuel-types")
    public Result<?> fuelTypeCreate(@RequestBody DictFuelType entity) {
        fuelTypeMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/fuel-types/{id}")
    public Result<?> fuelTypeUpdate(@PathVariable Long id, @RequestBody DictFuelType entity) {
        entity.setId(id);
        fuelTypeMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/fuel-types/{id}")
    public Result<?> fuelTypeDelete(@PathVariable Long id) {
        fuelTypeMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 颜色 Color ----------
    @GetMapping("/colors")
    public Result<?> colorList(@RequestParam(defaultValue = "1") Long pageNum,
                                @RequestParam(defaultValue = "20") Long pageSize) {
        return Result.success(colorMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.emptyWrapper()));
    }

    @PostMapping("/colors")
    public Result<?> colorCreate(@RequestBody DictColor entity) {
        colorMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/colors/{id}")
    public Result<?> colorUpdate(@PathVariable Long id, @RequestBody DictColor entity) {
        entity.setId(id);
        colorMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/colors/{id}")
    public Result<?> colorDelete(@PathVariable Long id) {
        colorMapper.deleteById(id);
        return Result.success();
    }

    // ---------- 促销标签 Pro-Label ----------
    @GetMapping("/pro-labels")
    public Result<?> proLabelList(@RequestParam(defaultValue = "1") Long pageNum,
                                   @RequestParam(defaultValue = "20") Long pageSize) {
        return Result.success(proLabelMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<ProLabel>lambdaQuery().orderByAsc(ProLabel::getSortNo)));
    }

    @GetMapping("/pro-labels/{id}")
    public Result<?> proLabelDetail(@PathVariable Long id) {
        ProLabel entity = proLabelMapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "Label not found");
        return Result.success(entity);
    }

    @PostMapping("/pro-labels")
    public Result<?> proLabelCreate(@RequestBody ProLabel entity) {
        proLabelMapper.insert(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/pro-labels/{id}")
    public Result<?> proLabelUpdate(@PathVariable Long id, @RequestBody ProLabel entity) {
        entity.setId(id);
        proLabelMapper.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/pro-labels/{id}")
    public Result<?> proLabelDelete(@PathVariable Long id) {
        proLabelMapper.deleteById(id);
        return Result.success();
    }
}
