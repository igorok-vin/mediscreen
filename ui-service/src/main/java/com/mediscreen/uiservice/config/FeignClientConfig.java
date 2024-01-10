package com.mediscreen.uiservice.config;

import com.mediscreen.uiservice.exception.CustomErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class FeignClientConfig  {
   @Bean
   public CustomErrorDecoder myCustomErrorDecoder() {
       return new CustomErrorDecoder();
   }

    @Bean
    public PageJacksonModule pageJacksonModule() {
        return new PageJacksonModule();
    }

    @Bean
    public SortJacksonModule sortJacksonModule() {
        return new SortJacksonModule();
    }

}
