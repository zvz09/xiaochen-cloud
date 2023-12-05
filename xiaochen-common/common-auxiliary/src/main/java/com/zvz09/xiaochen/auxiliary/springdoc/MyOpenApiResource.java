package com.zvz09.xiaochen.auxiliary.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.ObjectFactory;

import java.util.Locale;

/**
 * @author zvz09
 */
public class MyOpenApiResource extends OpenApiResource {

    public MyOpenApiResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
        super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
    }

    public OpenAPI getOpenApi() {
        return getOpenApi(Locale.getDefault());
    }

    @Override
    protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
        return null;
    }
}
