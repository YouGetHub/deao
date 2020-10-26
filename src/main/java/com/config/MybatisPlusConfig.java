package com.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * MybatisPlus 分页配置
 */
@Configuration
@ConditionalOnClass(value= {PaginationInterceptor.class})
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor  paginationInterceptor() {
        return new PaginationInterceptor();
    }

}