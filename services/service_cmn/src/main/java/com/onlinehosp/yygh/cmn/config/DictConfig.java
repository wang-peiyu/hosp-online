package com.onlinehosp.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//此类是为了让springboot能够扫描到项目中的mapper类

@Configuration
@MapperScan("com.onlinehosp.yygh.cmn.mapper")
public class DictConfig {
    /*
    * 分页插件
    * */

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
