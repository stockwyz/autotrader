package com.mcwilliamauto.modules.finance.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.finance.entity.FinanceSetting;
import com.mcwilliamauto.modules.finance.service.FinanceSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 后台设置：贷款计算器参数维护，需登录 */
@RestController
@RequestMapping("/api/admin/finance")
@RequiredArgsConstructor
public class AdminFinanceController {

    private final FinanceSettingService financeSettingService;

    @GetMapping("/setting")
    public Result<?> getSetting() {
        return Result.success(financeSettingService.list().stream().findFirst().orElse(new FinanceSetting()));
    }

    @PutMapping("/setting")
    public Result<?> saveSetting(@RequestBody FinanceSetting entity) {
        if (entity.getId() != null) {
            financeSettingService.updateById(entity);
        } else {
            financeSettingService.save(entity);
        }
        return Result.success();
    }
}
