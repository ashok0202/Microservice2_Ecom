package com.example.api_gateway.filters;

import com.example.api_gateway.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RouteValidator routeValidator;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Mapper

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!routeValidator.isSecured.test(exchange.getRequest())) {
                logger.info("Skipping filter for {}", exchange.getRequest().getPath());
                return chain.filter(exchange);
            }

            logger.info("Filtering request for {}", exchange.getRequest().getPath());

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header missing or invalid");
                return jsonErrorResponse(exchange, "Missing or invalid Authorization header.");
            }

            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isTokenExpired(token)) {
                    logger.warn("Token is expired");
                    return jsonErrorResponse(exchange, "Token has expired.");
                }

                String username = jwtUtil.extractUserName(token);
                ServerWebExchange updatedExchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-Authenticated-User", username)
                                .build())
                        .build();

                logger.info("User {} authenticated successfully", username);
                return chain.filter(updatedExchange);

            } catch (Exception e) {
                logger.error("Error processing token: {}", e.getMessage());
                return jsonErrorResponse(exchange, "Invalid token: " + e.getMessage());
            }
        };
    }

    private Mono<Void> jsonErrorResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString()); // Timestamp instead of status
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", message);
        responseBody.put("path", exchange.getRequest().getPath().value());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(responseBody);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public static class Config {
    }
}
