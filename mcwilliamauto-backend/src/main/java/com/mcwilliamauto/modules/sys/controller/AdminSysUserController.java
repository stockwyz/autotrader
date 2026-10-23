package com.mcwilliamauto.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcwilliamauto.common.BusinessException;
import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.sys.dto.SysUserSaveDTO;
import com.mcwilliamauto.modules.sys.entity.SysUser;
import com.mcwilliamauto.modules.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/** 后台用户管理，需登录 */
@RestController
@RequestMapping("/api/admin/sys-users")
@RequiredArgsConstructor
public class AdminSysUserController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") Long pageNum,
                           @RequestParam(defaultValue = "10") Long pageSize,
                           @RequestParam(required = false) String keyword) {
        Page<SysUser> page = sysUserService.page(new Page<>(pageNum, pageSize),
                Wrappers.<SysUser>lambdaQuery()
                        .like(StringUtils.hasText(keyword), SysUser::getUsername, keyword)
                        .orderByDesc(SysUser::getCreatedAt));
        // 密码哈希不返回给前端
        page.getRecords().forEach(u -> u.setPasswordHash(null));
        return Result.success(page);
    }

    @PostMapping
    public Result<?> create(@RequestBody SysUserSaveDTO dto) {
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new BusinessException("An initial password is required when creating a user");
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        sysUserService.save(user);
        return Result.success(user.getId());
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SysUserSaveDTO dto) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setId(id);
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        sysUserService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success();
    }
}
