package com.zvz09.xiaochen.auxiliary.config;

import com.zvz09.xiaochen.auxiliary.springdoc.MyOpenApiResource;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lizili-YF0033
 */
@Configuration(proxyBeanMethods = false)
public class MyOpenApiResourceConfiguration {
    @Bean
    MyOpenApiResource myOpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
                                        GenericResponseService responseBuilder, OperationService operationParser,
                                        SpringDocConfigProperties springDocConfigProperties,
                                        SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
        return new MyOpenApiResource("default", openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
    }
}
