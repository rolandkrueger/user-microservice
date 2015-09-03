package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public abstract class AbstractResource<T extends BaseApiData> extends AbstractRestClient {

    protected Link self;
    private final static HttpHeaders HEADERS;

    static {
        HEADERS = new HttpHeaders();
        HEADERS.setAccept(MediaType.parseMediaTypes("application/x-spring-data-verbose+json"));
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    public AbstractResource(Link self) {
        Preconditions.checkNotNull(self);
        this.self = self;
    }

    protected abstract Class<T> getResourceType();

    public final ResponseEntity<T> create(T entity) throws RestClientException {
        return restTemplate.exchange(
                self.expand().getHref(),
                HttpMethod.POST,
                new HttpEntity<>(entity, HEADERS),
                getParameterizedTypeReference());
    }

    public final void update(T entity) throws RestClientException {
        restTemplate.put(entity.getSelf().expand().getHref(), entity);
    }

    public final void delete(T entity) throws RestClientException {
        restTemplate.delete(entity.getSelf().expand().getHref());
    }

    protected abstract ParameterizedTypeReference<T> getParameterizedTypeReference();
}
