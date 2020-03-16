package com.actionzh.quartzdemo.config;

import ma.glasnost.orika.MapperFactory;

public interface MappingConfigurer {
    void configure(MapperFactory mapperFactory);
}
