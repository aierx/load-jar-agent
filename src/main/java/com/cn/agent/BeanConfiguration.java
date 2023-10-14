package com.cn.agent;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class BeanConfiguration {
    @Bean
    public ShowMeProcessor getShow(){
        return new ShowMeProcessor();
    }
}
