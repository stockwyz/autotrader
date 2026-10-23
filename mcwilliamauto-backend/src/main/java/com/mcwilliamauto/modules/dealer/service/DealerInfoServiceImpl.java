package com.mcwilliamauto.modules.dealer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mcwilliamauto.modules.dealer.entity.DealerInfo;
import com.mcwilliamauto.modules.dealer.mapper.DealerInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class DealerInfoServiceImpl extends ServiceImpl<DealerInfoMapper, DealerInfo> implements DealerInfoService {
}
