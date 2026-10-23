package com.mcwilliamauto.modules.lead.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.lead.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 后台查看/处理各类留资线索，需登录 */
@RestController
@RequestMapping("/api/admin/leads")
@RequiredArgsConstructor
public class AdminLeadController {

    private final LeadService leadService;

    @GetMapping("/inquiry")
    public Result<?> inquiry(@RequestParam(defaultValue = "1") Long pageNum,
                              @RequestParam(defaultValue = "10") Long pageSize,
                              @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageInquiry(pageNum, pageSize, status));
    }

    @GetMapping("/test-drive")
    public Result<?> testDrive(@RequestParam(defaultValue = "1") Long pageNum,
                                @RequestParam(defaultValue = "10") Long pageSize,
                                @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageTestDrive(pageNum, pageSize, status));
    }

    @GetMapping("/trade-in")
    public Result<?> tradeIn(@RequestParam(defaultValue = "1") Long pageNum,
                              @RequestParam(defaultValue = "10") Long pageSize,
                              @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageTradeIn(pageNum, pageSize, status));
    }

    @GetMapping("/car-finder")
    public Result<?> carFinder(@RequestParam(defaultValue = "1") Long pageNum,
                                @RequestParam(defaultValue = "10") Long pageSize,
                                @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageCarFinder(pageNum, pageSize, status));
    }

    @GetMapping("/contact")
    public Result<?> contact(@RequestParam(defaultValue = "1") Long pageNum,
                              @RequestParam(defaultValue = "10") Long pageSize,
                              @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageContact(pageNum, pageSize, status));
    }

    @GetMapping("/loan-application")
    public Result<?> loanApplication(@RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize,
                                      @RequestParam(required = false) Integer status) {
        return Result.success(leadService.pageLoanApplication(pageNum, pageSize, status));
    }

    @PatchMapping("/inquiry/{id}/status")
    public Result<?> updateInquiryStatus(@PathVariable Long id, @RequestParam Integer status) {
        leadService.updateInquiryStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/inquiry/{id}")
    public Result<?> deleteInquiry(@PathVariable Long id) {
        leadService.deleteInquiry(id);
        return Result.success();
    }

    @DeleteMapping("/test-drive/{id}")
    public Result<?> deleteTestDrive(@PathVariable Long id) {
        leadService.deleteTestDrive(id);
        return Result.success();
    }

    @DeleteMapping("/trade-in/{id}")
    public Result<?> deleteTradeIn(@PathVariable Long id) {
        leadService.deleteTradeIn(id);
        return Result.success();
    }

    @DeleteMapping("/car-finder/{id}")
    public Result<?> deleteCarFinder(@PathVariable Long id) {
        leadService.deleteCarFinder(id);
        return Result.success();
    }

    @DeleteMapping("/loan-application/{id}")
    public Result<?> deleteLoanApplication(@PathVariable Long id) {
        leadService.deleteLoanApplication(id);
        return Result.success();
    }

    @DeleteMapping("/contact/{id}")
    public Result<?> deleteContact(@PathVariable Long id) {
        leadService.deleteContact(id);
        return Result.success();
    }
}
