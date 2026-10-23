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

/** 前台公开接口：门店信息、营业时间 */
@RestController
@RequestMapping("/api/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final DealerInfoService dealerInfoService;
    private final DealerBusinessHoursService businessHoursService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    @GetMapping("/info")
    public Result<?> info() {
        // 门店信息只有一条记录，直接取第一条
        DealerInfo info = dealerInfoService.list(Wrappers.<DealerInfo>lambdaQuery().last("limit 1"))
                .stream().findFirst().orElse(null);
        if (info != null && StringUtils.hasText(info.getLogoUrl()) && !info.getLogoUrl().startsWith("http")) {
            info.setLogoUrl(baseUrl + accessPrefix + "/" + info.getLogoUrl());
        }
        return Result.success(info);
    }

    @GetMapping("/business-hours")
    public Result<?> businessHours() {
        return Result.success(businessHoursService.list(Wrappers.<DealerBusinessHours>lambdaQuery()
                .orderByAsc(DealerBusinessHours::getDayOfWeek)));
    }
}
