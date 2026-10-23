package com.mcwilliamauto.modules.dealer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.dealer.entity.DealerBusinessHours;
import com.mcwilliamauto.modules.dealer.entity.DealerInfo;
import com.mcwilliamauto.modules.dealer.service.DealerBusinessHoursService;
import com.mcwilliamauto.modules.dealer.service.DealerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 后台设置：门店信息与营业时间维护，需登录 */
@RestController
@RequestMapping("/api/admin/dealer")
@RequiredArgsConstructor
public class AdminDealerController {

    private final DealerInfoService dealerInfoService;
    private final DealerBusinessHoursService businessHoursService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    @GetMapping("/info")
    public Result<?> getInfo() {
        DealerInfo info = dealerInfoService.list(Wrappers.<DealerInfo>lambdaQuery().last("limit 1"))
                .stream().findFirst().orElse(new DealerInfo());
        if (StringUtils.hasText(info.getLogoUrl()) && !info.getLogoUrl().startsWith("http")) {
            //info.setLogoUrl(baseUrl + accessPrefix + "/" + info.getLogoUrl());
        }
        return Result.success(info);
    }

    /** 门店信息全局只有一条，有则更新、无则新增 */
    @PutMapping("/info")
    public Result<?> saveInfo(@RequestBody DealerInfo entity) {
        entity.setLogoUrl(normalizeUrl(entity.getLogoUrl()));
        if (entity.getId() != null) {
            dealerInfoService.updateById(entity);
        } else {
            dealerInfoService.save(entity);
        }
        return Result.success();
    }

    /** 单独保存前台网站风格(A~H)，只更新 theme_code 这一列，不影响门店其他信息 */
    @PutMapping("/theme")
    public Result<?> saveTheme(@RequestBody java.util.Map<String, String> body) {
        String code = body == null ? null : body.get("themeCode");
        if (code == null || !java.util.List.of("A", "B", "C", "D", "E", "F", "G", "H").contains(code)) {
            throw new com.mcwilliamauto.common.BusinessException("Invalid theme code");
        }
        DealerInfo info = dealerInfoService.list(Wrappers.<DealerInfo>lambdaQuery().last("limit 1"))
                .stream().findFirst().orElse(null);
        if (info == null) {
            info = new DealerInfo();
            info.setThemeCode(code);
            dealerInfoService.save(info);
        } else {
            DealerInfo upd = new DealerInfo();
            upd.setId(info.getId());
            upd.setThemeCode(code);
            dealerInfoService.updateById(upd);
        }
        return Result.success();
    }

    /** 不管前端传入的是完整http链接还是相对路径，统一转换成相对路径再入库 */
    private String normalizeUrl(String url) {
        if (!StringUtils.hasText(url)) return url;
        // 不管是完整http链接(http://host/uploads/x)还是上传接口直接返回的相对链接(/uploads/x)，
        // 只要包含 accessPrefix 就统一去掉，只留纯相对路径存库，避免读取时被重复拼接前缀
        String marker = accessPrefix + "/";
        int idx = url.indexOf(marker);
        if (idx >= 0) return url.substring(idx + marker.length());
        return url;
    }

    @GetMapping("/business-hours")
    public Result<?> listHours() {
        return Result.success(businessHoursService.list(Wrappers.<DealerBusinessHours>lambdaQuery()
                .orderByAsc(DealerBusinessHours::getDayOfWeek)));
    }

    /** 批量保存7天营业时间(整体覆盖) */
    @PutMapping("/business-hours")
    public Result<?> saveHours(@RequestBody List<DealerBusinessHours> hoursList) {
        businessHoursService.saveOrUpdateBatch(hoursList);
        return Result.success();
    }
}
