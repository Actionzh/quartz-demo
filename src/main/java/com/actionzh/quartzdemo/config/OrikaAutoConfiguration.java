package com.actionzh.quartzdemo.config;

import com.actionzh.quartzdemo.orikamapper.OrikaBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaAutoConfiguration {

    @Bean
    OrikaBeanMapper orikaBeanMapper() {
        return new OrikaBeanMapper();
    }
}
