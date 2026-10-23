package com.mcwilliamauto.modules.sys.controller;

import com.mcwilliamauto.common.Result;
import com.mcwilliamauto.modules.sys.dto.LoginDTO;
import com.mcwilliamauto.modules.sys.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
}
