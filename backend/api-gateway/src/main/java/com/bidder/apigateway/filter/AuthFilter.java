package com.bidder.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                System.out.println("DEBUG --> Request to a secured endpoint received: " + exchange.getRequest().getPath());
                
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    System.out.println("DEBUG -->  Missing authorization header in request to " + exchange.getRequest().getPath());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String token = null;
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    System.out.println("DEBUG --> Token extracted successfully for request to " + exchange.getRequest().getPath());
                } else {
                    System.out.println("DEBUG --> Authorization header format is invalid in request to " + exchange.getRequest().getPath());
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(token);
                HttpEntity<String> request = new HttpEntity<>(headers);

                try {
                    ResponseEntity<?> response = template.postForEntity("http://api-gateway:8080/api/v1/auth/validateToken", request, String.class);
                    if (response.getStatusCode() != HttpStatus.OK) {
                        System.out.println("DEBUG -->  Token validation failed for request to " + exchange.getRequest().getPath());
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    } else {
                        System.out.println("DEBUG --> Token validation succeeded for request to " + exchange.getRequest().getPath());
                    }
                } catch (HttpClientErrorException e) {
                    System.out.println("DEBUG -->  HttpClientErrorException caught during token validation: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                } catch (Exception e) {
                    System.out.println("DEBUG --> Exception caught during token validation: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                }
            } else {
                System.out.println("DEBUG --> Request to an open endpoint received: " + exchange.getRequest().getPath());
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
