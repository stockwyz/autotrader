package com.mcwilliamauto.modules.dict.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.dict.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @GetMapping("/makes")
    public Result<?> makes() {
        return Result.success(dictService.listMakes());
    }

    @GetMapping("/models")
    public Result<?> models(@RequestParam(required = false) Long makeId) {
        return Result.success(dictService.listModelsByMake(makeId));
    }

    @GetMapping("/body-styles")
    public Result<?> bodyStyles() {
        return Result.success(dictService.listBodyStyles());
    }

    @GetMapping("/transmissions")
    public Result<?> transmissions() {
        return Result.success(dictService.listTransmissions());
    }

    @GetMapping("/engines")
    public Result<?> engines() {
        return Result.success(dictService.listEngines());
    }

    @GetMapping("/fuel-types")
    public Result<?> fuelTypes() {
        return Result.success(dictService.listFuelTypes());
    }

    @GetMapping("/colors")
    public Result<?> colors() {
        return Result.success(dictService.listColors());
    }
}
