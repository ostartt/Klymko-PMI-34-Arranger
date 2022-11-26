package com.arranger.eurekaclient.config;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {

    @Autowired
    private SpringClientFactory springClientFactory;

    @Bean
    public IRule ribbonRule() {
        return new BestAvailableRule();
    }

}