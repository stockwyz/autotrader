package com.mcwilliamauto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mcwilliamauto.modules.*.mapper")
public class McwilliamautoApplication {
    public static void main(String[] args) {
        SpringApplication.run(McwilliamautoApplication.class, args);
    }
}
