package com.mcwilliamauto.modules.dealer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mcwilliamauto.modules.dealer.entity.DealerBusinessHours;
import com.mcwilliamauto.modules.dealer.mapper.DealerBusinessHoursMapper;
import org.springframework.stereotype.Service;

@Service
public class DealerBusinessHoursServiceImpl extends ServiceImpl<DealerBusinessHoursMapper, DealerBusinessHours> implements DealerBusinessHoursService {
}
