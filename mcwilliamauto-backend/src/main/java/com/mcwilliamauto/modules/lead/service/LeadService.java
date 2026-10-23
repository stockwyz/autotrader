package com.mcwilliamauto.modules.lead.service;

import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.modules.lead.dto.*;

public interface LeadService {
    void submitInquiry(LeadInquiryDTO dto);
    void submitTestDrive(LeadTestDriveDTO dto);
    void submitTradeIn(LeadTradeInDTO dto);
    void submitCarFinder(LeadCarFinderDTO dto);
    void submitContact(LeadContactDTO dto);
    void submitLoanApplication(LeadLoanApplicationDTO dto);

    PageResult<?> pageInquiry(Long pageNum, Long pageSize, Integer status);
    PageResult<?> pageTestDrive(Long pageNum, Long pageSize, Integer status);
    PageResult<?> pageTradeIn(Long pageNum, Long pageSize, Integer status);
    PageResult<?> pageCarFinder(Long pageNum, Long pageSize, Integer status);
    PageResult<?> pageContact(Long pageNum, Long pageSize, Integer status);
    PageResult<?> pageLoanApplication(Long pageNum, Long pageSize, Integer status);

    void updateInquiryStatus(Long id, Integer status);

    void deleteInquiry(Long id);
    void deleteTestDrive(Long id);
    void deleteTradeIn(Long id);
    void deleteCarFinder(Long id);
    void deleteLoanApplication(Long id);
    void deleteContact(Long id);
}
