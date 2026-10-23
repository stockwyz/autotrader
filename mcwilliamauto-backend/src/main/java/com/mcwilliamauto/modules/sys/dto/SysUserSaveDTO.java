package com.mcwilliamauto.modules.sys.dto;

import lombok.Data;

@Data
public class SysUserSaveDTO {
    private Long id;
    private String username;
    /** 新增或重置密码时传，编辑时留空表示不修改密码 */
    private String password;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
}
