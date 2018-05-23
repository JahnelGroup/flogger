package com.jahnelgroup.flogger.spring.boot.autoconfigure;

import com.jahnelgroup.flogger.spi.BindParamAspectImpl;
import com.jahnelgroup.flogger.spi.BindReturnAspectImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({BindReturnAspectImpl.class, BindReturnAspectImpl.class})
public class FloggerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BindParamAspectImpl bindParamAspect() {
        return new BindParamAspectImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public BindReturnAspectImpl bindReturnAspect() {
        return new BindReturnAspectImpl();
    }
}
