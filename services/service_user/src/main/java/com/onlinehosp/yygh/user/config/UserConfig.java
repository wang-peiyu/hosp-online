package com.onlinehosp.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.onlinehosp.yygh.user.mapper")
public class UserConfig {
}
