package info.rolandkrueger.userservice.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Roland Krüger
 */
@Service
public class StaticEndpointProvider implements UserMicroserviceEndpointProvider {

    private String endpoint;

    @Override
    public String getUserMicroserviceEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
