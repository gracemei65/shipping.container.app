/**
 * ﻿Copyright ©2018 Sensus
 * All rights reserved
 */
package com.railinc.shipping.container.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AppSwagger2Config {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.railinc.shipping.container")).build();
    }

    //    /**
    //     * ignore null fields when serializing a java class globally
    //     *
    //     * @return
    //     */
    //    @Bean
    //    public ObjectMapper objectMapper() {
    //        ObjectMapper mapper = new ObjectMapper();
    //        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    //        return mapper;
    //    }
}
