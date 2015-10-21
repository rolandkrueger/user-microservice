package info.rolandkrueger.userservice.api.service;

/**
 * Interface for implementing providers that dynamically provide an endpoint URL for the
 * user micro-service. Such a provider can be useful, for instance, in a load-balancing scenario
 * where there are several instances of the service running behind a load-balancer. The endpoint
 * provider could then deal out the endpoint addresses of these service instance in a round-robin
 * fashion.
 *
 * @author Roland Krüger
 */
public interface UserMicroserviceEndpointProvider {
    /**
     * Returns the endpoint address of a user micro-service instance.
     *
     * @return an endpoint address of the user service
     */
    String getUserMicroserviceEndpoint();
}
