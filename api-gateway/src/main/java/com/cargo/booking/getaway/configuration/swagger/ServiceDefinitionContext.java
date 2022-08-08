package com.cargo.booking.getaway.configuration.swagger;

import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Контекст для хранения ресурсов свагера.
 */
@Component
public class ServiceDefinitionContext {

    private final ConcurrentHashMap<String, String> swaggrResources;

    /**
     * Конструктор.
     */
    public ServiceDefinitionContext() {
        this.swaggrResources = new ConcurrentHashMap<String,String>();
    }

    /**
     * Метод добавления ресурса.
     * @param name имя ресурса.
     * @param content контент ресурса.
     */
    public void addServiceDefinition(String name, String content) {
        swaggrResources.put(name, content);
    }


    /**
     * Получение списка всех ресурсов.
     * @return список ресурсов.
     */
    public List<SwaggerResource> getSwaggerDefinitions() {

        return swaggrResources.entrySet().stream().map(service -> {
            SwaggerResource resource = new SwaggerResource();
            resource.setName(service.getKey());
            resource.setUrl("/service/"+service.getKey());
            resource.setSwaggerVersion("3.0");
            return resource;
        }).collect(Collectors.toList());
    }

    /**
     * Получение ресурса по имени
     * @param serviceName имя ресурса
     * @return ресурс
     */
    public String getServiceDefinition(String serviceName) {
        return swaggrResources.get(serviceName);
    }

}
