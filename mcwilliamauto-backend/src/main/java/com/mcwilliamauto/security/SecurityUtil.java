package com.mcwilliamauto.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static LoginUser getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication() == null
                ? null : SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUser) {
            return (LoginUser) principal;
        }
        return null;
    }

    public static Long getUserId() {
        LoginUser user = getLoginUser();
        return user == null ? null : user.getUserId();
    }
}
