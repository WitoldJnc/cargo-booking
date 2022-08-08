package com.cargo.booking.internal.gateway.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Обновление ресурсов свагера.
 */
@Component
@Slf4j
public class ServiceDescriptionUpdater {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ServiceDefinitionContext definitionContext;

    @Value("${swagger.config.gateway.url}")
    private String gatewayUrl;

    private final RestTemplate template;

    /**
     * Конструктор.
     */
    public ServiceDescriptionUpdater() {
        this.template = new RestTemplate();
    }

    /**
     * Шедуллер обновления ресурсов.
     */
    @Scheduled(fixedDelayString = "${swagger.config.refreshrate}")
    public void refreshSwaggerConfigurations() {
        log.info("Start update swagger description...");
        discoveryClient.getServices().stream().forEach(serviceId -> {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
            if (serviceInstances == null || serviceInstances.isEmpty()) {
                log.info("No instances available for service : {} ", serviceId);
            }else {
                try {
                    ServiceInstance instance = serviceInstances.get(0);
                    String swaggerURL = instance.getUri() + "/v3/api-docs";
                    Object jsonData = template.getForObject(swaggerURL, Object.class);
                    String content = getJSON(jsonData);
                    content = content.replaceAll(instance.getUri().toString(),
                            gatewayUrl + serviceId);
                    definitionContext.addServiceDefinition(serviceId, content);
                }
                catch (Exception ex){
                    log.info("No swagger available for service : {} ", serviceId);
                }
            }});
        log.info("End update description...");
    }

    /**
     * Преобразование json  в строку.
     * @param jsonData данные в формате json.
     * @return строка контента.
     */
    private String getJSON(Object jsonData) {
        try {
            return new ObjectMapper().writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
