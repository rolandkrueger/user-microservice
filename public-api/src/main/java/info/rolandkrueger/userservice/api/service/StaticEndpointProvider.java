package info.rolandkrueger.userservice.api.service;

import org.springframework.stereotype.Service;

/**
 * Default implementation for the {@link UserMicroserviceEndpointProvider} that simply provides a static, predefined
 * user service endpoint URL.
 *
 * @author Roland Krüger
 */
@Service
public class StaticEndpointProvider implements UserMicroserviceEndpointProvider {

    private String endpoint;

    protected StaticEndpointProvider() {
    }

    /**
     * Creates a new StaticEndpointProvider for the given static endpoint URL.
     *
     * @param endpoint static endpoint address for the user micro-service
     */
    public StaticEndpointProvider(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Returns the fixed user service endpoint address passed in through the constructor.
     */
    @Override
    public String getUserMicroserviceEndpoint() {
        return endpoint;
    }
}
