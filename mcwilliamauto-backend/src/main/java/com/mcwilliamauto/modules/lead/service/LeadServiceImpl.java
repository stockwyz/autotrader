package com.mcwilliamauto.modules.lead.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.PageResult;
import com.mcwilliamauto.modules.lead.dto.*;
import com.mcwilliamauto.modules.lead.entity.*;
import com.mcwilliamauto.modules.lead.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadInquiryMapper leadInquiryMapper;
    private final LeadTestDriveMapper leadTestDriveMapper;
    private final LeadTradeInMapper leadTradeInMapper;
    private final LeadCarFinderMapper leadCarFinderMapper;
    private final LeadContactMapper leadContactMapper;
    private final LeadLoanApplicationMapper leadLoanApplicationMapper;

    @Override
    public void submitInquiry(LeadInquiryDTO dto) {
        LeadInquiry entity = new LeadInquiry();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        leadInquiryMapper.insert(entity);
    }

    @Override
    public void submitTestDrive(LeadTestDriveDTO dto) {
        LeadTestDrive entity = new LeadTestDrive();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        leadTestDriveMapper.insert(entity);
    }

    @Override
    public void submitTradeIn(LeadTradeInDTO dto) {
        LeadTradeIn entity = new LeadTradeIn();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        leadTradeInMapper.insert(entity);
    }

    @Override
    public void submitCarFinder(LeadCarFinderDTO dto) {
        LeadCarFinder entity = new LeadCarFinder();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        leadCarFinderMapper.insert(entity);
    }

    @Override
    public void submitContact(LeadContactDTO dto) {
        LeadContact entity = new LeadContact();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        leadContactMapper.insert(entity);
    }

    @Override
    public void submitLoanApplication(LeadLoanApplicationDTO dto) {
        LeadLoanApplication entity = new LeadLoanApplication();
        BeanUtils.copyProperties(dto, entity);
        entity.setJobTitle(dto.getOccupation());
        if (dto.getGrossIncome() != null) {
            // 生产环境应使用对称加密(如AES)存储，此处为演示直接转字符串存储
            entity.setMonthlyIncomeEnc(dto.getGrossIncome().toPlainString());
        }
        if (dto.getSin() != null && !dto.getSin().isBlank()) {
            // 生产环境应使用对称加密(如AES)存储，此处为演示直接转字符串存储
            entity.setSinEnc(dto.getSin());
        }
        entity.setStatus(0);
        leadLoanApplicationMapper.insert(entity);
    }

    @Override
    public PageResult<?> pageInquiry(Long pageNum, Long pageSize, Integer status) {
        Page<LeadInquiry> page = leadInquiryMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadInquiry>lambdaQuery()
                        .eq(status != null, LeadInquiry::getStatus, status)
                        .orderByDesc(LeadInquiry::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<?> pageTestDrive(Long pageNum, Long pageSize, Integer status) {
        Page<LeadTestDrive> page = leadTestDriveMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadTestDrive>lambdaQuery()
                        .eq(status != null, LeadTestDrive::getStatus, status)
                        .orderByDesc(LeadTestDrive::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<?> pageTradeIn(Long pageNum, Long pageSize, Integer status) {
        Page<LeadTradeIn> page = leadTradeInMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadTradeIn>lambdaQuery()
                        .eq(status != null, LeadTradeIn::getStatus, status)
                        .orderByDesc(LeadTradeIn::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<?> pageCarFinder(Long pageNum, Long pageSize, Integer status) {
        Page<LeadCarFinder> page = leadCarFinderMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadCarFinder>lambdaQuery()
                        .eq(status != null, LeadCarFinder::getStatus, status)
                        .orderByDesc(LeadCarFinder::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<?> pageContact(Long pageNum, Long pageSize, Integer status) {
        Page<LeadContact> page = leadContactMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadContact>lambdaQuery()
                        .eq(status != null, LeadContact::getStatus, status)
                        .orderByDesc(LeadContact::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public PageResult<?> pageLoanApplication(Long pageNum, Long pageSize, Integer status) {
        Page<LeadLoanApplication> page = leadLoanApplicationMapper.selectPage(new Page<>(pageNum, pageSize),
                Wrappers.<LeadLoanApplication>lambdaQuery()
                        .eq(status != null, LeadLoanApplication::getStatus, status)
                        .orderByDesc(LeadLoanApplication::getCreatedAt));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    @Override
    public void updateInquiryStatus(Long id, Integer status) {
        LeadInquiry update = new LeadInquiry();
        update.setId(id);
        update.setStatus(status);
        leadInquiryMapper.updateById(update);
    }

    @Override
    public void deleteInquiry(Long id) {
        leadInquiryMapper.deleteById(id);
    }

    @Override
    public void deleteTestDrive(Long id) {
        leadTestDriveMapper.deleteById(id);
    }

    @Override
    public void deleteTradeIn(Long id) {
        leadTradeInMapper.deleteById(id);
    }

    @Override
    public void deleteCarFinder(Long id) {
        leadCarFinderMapper.deleteById(id);
    }

    @Override
    public void deleteLoanApplication(Long id) {
        leadLoanApplicationMapper.deleteById(id);
    }

    @Override
    public void deleteContact(Long id) {
        leadContactMapper.deleteById(id);
    }
}
