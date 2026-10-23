package com.mcwilliamauto.modules.lead.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.lead.dto.*;
import com.mcwilliamauto.modules.lead.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 前台各类留资表单提交入口，全部公开无需登录 */
@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    /** 车辆详情页 "Request More Information" */
    @PostMapping("/inquiry")
    public Result<?> inquiry(@Valid @RequestBody LeadInquiryDTO dto) {
        leadService.submitInquiry(dto);
        return Result.success();
    }

    /** "Test Drive This Vehicle" */
    @PostMapping("/test-drive")
    public Result<?> testDrive(@Valid @RequestBody LeadTestDriveDTO dto) {
        leadService.submitTestDrive(dto);
        return Result.success();
    }

    /** "Trade-In Appraisal" */
    @PostMapping("/trade-in")
    public Result<?> tradeIn(@Valid @RequestBody LeadTradeInDTO dto) {
        leadService.submitTradeIn(dto);
        return Result.success();
    }

    /** "Car Finder" */
    @PostMapping("/car-finder")
    public Result<?> carFinder(@Valid @RequestBody LeadCarFinderDTO dto) {
        leadService.submitCarFinder(dto);
        return Result.success();
    }

    /** "Contact Us" / "Text Us Now" 通过 type 字段区分 */
    @PostMapping("/contact")
    public Result<?> contact(@Valid @RequestBody LeadContactDTO dto) {
        leadService.submitContact(dto);
        return Result.success();
    }

    /** "Apply For Credit" 信贷申请 */
    @PostMapping("/loan-application")
    public Result<?> loanApplication(@Valid @RequestBody LeadLoanApplicationDTO dto) {
        leadService.submitLoanApplication(dto);
        return Result.success();
    }
}
