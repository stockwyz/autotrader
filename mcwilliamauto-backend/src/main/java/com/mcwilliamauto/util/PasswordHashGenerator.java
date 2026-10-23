package com.mcwilliamauto.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 独立小工具：生成后台管理员初始密码的 BCrypt 哈希值。
 * 用法：直接运行本类的 main 方法（无需启动整个 Spring Boot 应用），
 * 将输出的哈希值填入 sql/schema.sql 中 sys_user 表的初始化 INSERT 语句。
 *
 * 示例：
 *   java -cp target/classes com.mcwilliamauto.util.PasswordHashGenerator admin123
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        String rawPassword = args.length > 0 ? args[0] : "admin123";
        String hash = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt哈希: " + hash);
        System.out.println("请将上面的哈希值填入 sys_user 表 password_hash 字段");
    }
}
