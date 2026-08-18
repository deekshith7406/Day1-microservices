package com.oneenterprise.orderservice.client;

import com.oneenterprise.orderservice.dto.UserSummaryDto;
import com.oneenterprise.orderservice.exception.RelatedUserNotFoundException;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * The one place in Order Service that knows how to talk to User Service.
 * The base URL comes from configuration (application.yml / env override),
 * per the handbook's requirement not to hard-code it in multiple places.
 */
@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceBaseUrl;

    public UserServiceClient(RestTemplate restTemplate,
                              @Value("${user-service.base-url}") String userServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    public UserSummaryDto getUserById(Long userId) {
        String url = userServiceBaseUrl + "/api/users/" + userId;
        try {
            return restTemplate.getForObject(url, UserSummaryDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            // User Service responded, and it said "no such user" — 404 is a
            // legitimate business outcome, not an infrastructure failure.
            throw new RelatedUserNotFoundException(userId);
        } catch (ResourceAccessException ex) {
            // Connection refused, DNS failure, or read/connect timeout —
            // User Service is down, unreachable, or too slow.
            throw new UserServiceUnavailableException(
                    "Could not connect to User Service at " + userServiceBaseUrl, ex);
        } catch (HttpClientErrorException | org.springframework.web.client.HttpServerErrorException ex) {
            // Any other 4xx/5xx from User Service — treat as unavailable
            // from Order Service's point of view.
            throw new UserServiceUnavailableException(
                    "User Service returned an unexpected error: " + ex.getStatusCode(), ex);
        }
    }
}
