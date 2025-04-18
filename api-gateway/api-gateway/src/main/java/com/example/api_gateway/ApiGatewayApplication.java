package com.example.api_gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;


import java.time.Duration;


@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	KeyResolver keyResolver() {
		return exchange -> Mono.just ("userKey");
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> customizer() {
		return factory -> factory.configureDefault(id ->
				new Resilience4JConfigBuilder(id)
						.circuitBreakerConfig(CircuitBreakerConfig.custom()
								.failureRateThreshold(50)             // Circuit opens if failure rate > 50%
								.waitDurationInOpenState(Duration.ofSeconds(10)) // Wait time before retry
								.slidingWindowSize(5)            // Monitor last 5 calls
								.build()
						)
						.build()
		);
	}
}
