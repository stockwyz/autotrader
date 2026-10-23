package com.mcwilliamauto.modules.sys.service;

import com.mcwilliamauto.modules.sys.dto.LoginDTO;
import com.mcwilliamauto.modules.sys.dto.LoginVO;

public interface AuthService {
    LoginVO login(LoginDTO dto);
}
