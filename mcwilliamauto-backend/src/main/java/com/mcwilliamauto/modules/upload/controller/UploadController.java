package com.mcwilliamauto.modules.upload.controller;

import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** 图片/文件上传：本地磁盘存储，同时生成缩略图（对应原站 pic-xxx / thumb-xxx 两套图） */
@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
public class UploadController {

    private final VehicleService vehicleService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    /** 上传车辆图片并直接关联到车辆(multipart多文件) */
    @PostMapping("/vehicle/{vehicleId}/images")
    public Result<?> uploadVehicleImages(@PathVariable Long vehicleId,
                                          @RequestParam("files") MultipartFile[] files) throws IOException {
        String stockNo = getStockNoOrThrow(vehicleId);
        File dir = new File(uploadDir, stockNo);
        if (!dir.exists()) dir.mkdirs();

        List<String[]> pairs = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String ext = getExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String picName = "pic-" + uuid + ext;
            String thumbName = "thumb-" + uuid + ext;

            File picFile = new File(dir, picName);
            file.transferTo(picFile);

            File thumbFile = new File(dir, thumbName);
            Thumbnails.of(picFile).size(400, 300).keepAspectRatio(true).toFile(thumbFile);

            String picUrl = accessPrefix + "/" + stockNo + "/" + picName;
            String thumbUrl = accessPrefix + "/" + stockNo + "/" + thumbName;
            pairs.add(new String[]{picUrl, thumbUrl});
        }
        List<com.mcwilliamauto.modules.vehicle.entity.VehicleImage> created = vehicleService.attachImages(vehicleId, pairs);
        List<Map<String, Object>> result = new ArrayList<>();
        for (com.mcwilliamauto.modules.vehicle.entity.VehicleImage img : created) {
            result.add(Map.of("id", img.getId(), "imageUrl", img.getImageUrl(), "thumbUrl", img.getThumbUrl()));
        }
        return Result.success(result);
    }

    /** 上传车辆视频，跟图片存进同一个以 stockNo 命名的目录 */
    @PostMapping("/vehicle/{vehicleId}/video")
    public Result<?> uploadVehicleVideo(@PathVariable Long vehicleId,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new BusinessException("File is required");

        String stockNo = getStockNoOrThrow(vehicleId);
        File dir = new File(uploadDir, stockNo);
        if (!dir.exists()) dir.mkdirs();

        String ext = getExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = "video-" + uuid + ext;
        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 新视频文件已经落盘成功了，这时候再去删旧视频的物理文件，避免"旧文件删了、新文件却没传成功"这种更糟的中间状态
        vehicleService.deleteOldVideoFile(vehicleId);

        String relativePath = stockNo + "/" + fileName;
        String url = accessPrefix + "/" + relativePath;
        return Result.success(Map.of("path", relativePath, "url", url));
    }

    /** 上传CARFAX报告PDF，固定文件名carfax.pdf，跟车辆图片/视频存进同一个以stockNo命名的目录；重复上传会直接覆盖旧文件 */
    @PostMapping("/vehicle/{vehicleId}/carfax")
    public Result<?> uploadVehicleCarfax(@PathVariable Long vehicleId,
                                          @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new BusinessException("File is required");
        String original = file.getOriginalFilename();
        boolean isPdf = "application/pdf".equalsIgnoreCase(file.getContentType())
                || (original != null && original.toLowerCase().endsWith(".pdf"));
        if (!isPdf) throw new BusinessException("Only PDF files are allowed");

        String stockNo = getStockNoOrThrow(vehicleId);
        File dir = new File(uploadDir, stockNo);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(dir, "carfax.pdf");
        file.transferTo(dest);

        String relativePath = stockNo + "/carfax.pdf";
        String url = accessPrefix + "/" + relativePath;
        return Result.success(Map.of("path", relativePath, "url", url));
    }

    /** 根据车辆ID查出stockNo作为该车图片/视频的存储子目录名，查不到就直接报错中止上传 */
    private String getStockNoOrThrow(Long vehicleId) {
        String stockNo = vehicleService.getStockNo(vehicleId);
        if (stockNo == null || stockNo.isBlank()) {
            throw new BusinessException("Vehicle not found or missing stock number");
        }
        return stockNo;
    }

    /** 上传CARFAX PDF等其他文件，返回可访问URL */
    @PostMapping("/file")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new BusinessException("File is required");
        String subDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        File dir = new File(uploadDir, subDir);
        if (!dir.exists()) dir.mkdirs();

        String ext = getExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;
        File dest = new File(dir, fileName);
        file.transferTo(dest);

        String url = accessPrefix + "/" + subDir + "/" + fileName;
        return Result.success(Map.of("url", url));
    }

    /** 上传促销标签(Pro-Label)图片，全局共用，固定存入 upload-dir 下的 label 子目录 */
    @PostMapping("/label-image")
    public Result<?> uploadLabelImage(@RequestParam("file") MultipartFile file) throws IOException {
        return saveToSubdir(file, "label", "img");
    }

    /** 上传首页轮播Banner图，固定存入 upload-dir 下的 banner 子目录 */
    @PostMapping("/banner-image")
    public Result<?> uploadBannerImage(@RequestParam("file") MultipartFile file) throws IOException {
        return saveToSubdir(file, "banner", "img");
    }

    /** 上传品牌Logo，固定存入 upload-dir 下的 make 子目录 */
    @PostMapping("/make-logo")
    public Result<?> uploadMakeLogo(@RequestParam("file") MultipartFile file) throws IOException {
        return saveToSubdir(file, "make", "logo");
    }

    /** 上传车身类型示例图片，固定存入 upload-dir 下的 bodystyle 子目录 */
    @PostMapping("/bodystyle-photo")
    public Result<?> uploadBodyStylePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        return saveToSubdir(file, "bodystyle", "logo");
    }

    /** 上传门店Logo，固定存入 upload-dir 下的 dealer 子目录，文件名前缀用门店名称方便识别 */
    @PostMapping("/dealer-logo")
    public Result<?> uploadDealerLogo(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "name", required = false) String dealerName) throws IOException {
        String prefix = (dealerName == null || dealerName.isBlank())
                ? "dealer"
                : dealerName.toLowerCase().trim().replaceAll("[^a-z0-9]+", "-");
        return saveToSubdir(file, "dealer", prefix);
    }

    /** 富文本页面(About Us等)编辑器内插入的图片，固定存入 upload-dir 下的 page-content 子目录 */
    @PostMapping("/page-content-image")
    public Result<?> uploadPageContentImage(@RequestParam("file") MultipartFile file) throws IOException {
        return saveToSubdir(file, "page-content", "img");
    }

    /**
     * 通用保存逻辑：文件名用"传入前缀+时间戳"，不保留原文件名（避免中文名等编码问题），
     * 时间戳保证每次都是全新文件名、永不覆盖已存在的文件——
     * 这是踩过Windows下"覆盖写入被其他进程占用的旧文件导致失败"这个坑之后统一定下的安全做法。
     */
    private Result<?> saveToSubdir(MultipartFile file, String subdir, String namePrefix) throws IOException {
        if (file.isEmpty()) throw new BusinessException("File is required");

        File dir = new File(uploadDir, subdir);
        if (!dir.exists()) dir.mkdirs();

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot >= 0) {
                ext = original.substring(dot).toLowerCase().replaceAll("[^a-z0-9.]", "");
            }
        }
        String safeName = namePrefix + "-" + System.currentTimeMillis() + ext;
        File dest = new File(dir, safeName);

        byte[] bytes = file.getBytes();
        java.nio.file.Files.write(dest.toPath(), bytes);

        String relativePath = subdir + "/" + safeName;
        String url = accessPrefix + "/" + relativePath;
        return Result.success(Map.of("path", relativePath, "url", url));
    }

    /** 删除品牌Logo文件（可选调用，只是清理磁盘文件，数据库字段清空由前端单独调用更新接口） */
    @DeleteMapping("/make-logo")
    public Result<?> deleteMakeLogo(@RequestParam String path) {
        File target = new File(uploadDir, path);
        if (target.exists() && target.isFile()) {
            target.delete();
        }
        return Result.success();
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.'));
    }
}
