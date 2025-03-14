package com.teekworks.Order_Service;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


/**
 * A test implementation of ServiceInstanceListSupplier.
 */
public class TestServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    /**
     * Returns an empty string, indicating that no service ID is provided.
     */
    @Override
    public String getServiceId() {
        return "";
    }

    /**
     * Returns null, indicating that no list of service instances is available.
     */
    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> serviceInstances = new ArrayList<>();

        serviceInstances.add(new DefaultServiceInstance(
                "PAYMENT-SERVICE",
                "PAYMENT-SERVICE",
                "localhost",
                8082,
                false
        ));

        serviceInstances.add(new DefaultServiceInstance(
                "PRODUCT-SERVICE",
                "PRODUCT-SERVICE",
                "localhost",
                8081,
                false
        ));
        return Flux.just(serviceInstances);
    }
}
