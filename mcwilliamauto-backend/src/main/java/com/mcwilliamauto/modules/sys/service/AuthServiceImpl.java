package com.mcwilliamauto.modules.sys.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.modules.sys.dto.LoginDTO;
import com.mcwilliamauto.modules.sys.dto.LoginVO;
import com.mcwilliamauto.modules.sys.entity.SysUser;
import com.mcwilliamauto.modules.sys.mapper.SysUserMapper;
import com.mcwilliamauto.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO dto) {
        SysUser user = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (user == null || user.getStatus() == 0) {
            throw new BusinessException(401, "Invalid username or password");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(401, "Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        SysUser update = new SysUser();
        update.setId(user.getId());
        update.setLastLoginAt(LocalDateTime.now());
        sysUserMapper.updateById(update);

        return new LoginVO(token, user.getId(), user.getUsername(), user.getRealName());
    }
}
