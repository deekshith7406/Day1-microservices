package com.oneenterprise.orderservice.client;

import com.oneenterprise.orderservice.dto.UserSummaryDto;
import com.oneenterprise.orderservice.exception.RelatedUserNotFoundException;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

onent
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
          
            throw new RelatedUserNotFoundException(userId);
        } catch (ResourceAccessException ex) {
           
            throw new UserServiceUnavailableException(
                    "Could not connect to User Service at " + userServiceBaseUrl, ex);
        } catch (HttpClientErrorException | org.springframework.web.client.HttpServerErrorException ex) {
           
            throw new UserServiceUnavailableException(
                    "User Service returned an unexpected error: " + ex.getStatusCode(), ex);
        }
    }
}
