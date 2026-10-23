package com.mcwilliamauto.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mcwilliamauto.modules.sys.entity.SysUser;
import com.mcwilliamauto.modules.sys.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
