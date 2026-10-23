package com.mcwilliamauto.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mcwilliamauto.modules.finance.entity.FinanceSetting;
import com.mcwilliamauto.modules.finance.mapper.FinanceSettingMapper;
import org.springframework.stereotype.Service;

@Service
public class FinanceSettingServiceImpl extends ServiceImpl<FinanceSettingMapper, FinanceSetting> implements FinanceSettingService {
}
