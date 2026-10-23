package com.mcwilliamauto.modules.finance.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.finance.service.FinanceSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 前台公开接口：贷款计算器默认参数 */
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceSettingService financeSettingService;

    @GetMapping("/calculator-params")
    public Result<?> calculatorParams() {
        return Result.success(financeSettingService.list().stream().findFirst().orElse(null));
    }
}
