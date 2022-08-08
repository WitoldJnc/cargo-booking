package com.cargo.booking.getaway.configuration.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Кнфигурация свагера.
 */
@Configuration
public class SwaggerUIConfiguration {

    @Autowired
    private ServiceDefinitionContext serviceDefnContxt;

    /**
     *
     * @param provider добавление ресурсов в провайдер.
     * @return провайдер
     */
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResource(InMemorySwaggerResourcesProvider provider) {

        return () ->{
            List<SwaggerResource> resources = new ArrayList<>(provider.get());
            resources.clear();
            resources.addAll(serviceDefnContxt.getSwaggerDefinitions());
            return resources;
        };
    }
}
