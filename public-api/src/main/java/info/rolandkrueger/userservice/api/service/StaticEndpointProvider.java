package info.rolandkrueger.userservice.api.service;

import org.springframework.stereotype.Service;

/**
 * @author Roland Krüger
 */
@Service
public class StaticEndpointProvider implements UserMicroserviceEndpointProvider {

    private String endpoint;

    protected StaticEndpointProvider() {
    }

    public StaticEndpointProvider(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getUserMicroserviceEndpoint() {
        return endpoint;
    }
}
