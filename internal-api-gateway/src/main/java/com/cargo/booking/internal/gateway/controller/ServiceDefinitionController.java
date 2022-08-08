package com.cargo.booking.internal.gateway.controller;

import com.cargo.booking.internal.gateway.swagger.ServiceDefinitionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер ресурсов свагера.
 */
@RestController
public class ServiceDefinitionController {

    @Autowired
    ServiceDefinitionContext context;

    /**
     * Получение ресурса по имен.
     * @param serviceName имя ресурса.
     * @return ресурс.
     */
    @GetMapping("/service/{serviceName}")
    public String getServiceDefinition(@PathVariable("serviceName") String serviceName) {
        return context.getServiceDefinition(serviceName);
    }
}
