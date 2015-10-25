package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Roland Krüger
 */
public abstract class AbstractResource<T extends AbstractBaseApiData<?>> extends AbstractRestClient {

    protected Link self;
    protected Link templatedBaseLink;
    private ResponseEntity<T> responseEntity;
    private T data;

    protected AbstractResource(Link templatedBaseLink, T data) {
        this(templatedBaseLink);
        this.data = data;
    }

    public AbstractResource(Link templatedBaseLink) {
        this(templatedBaseLink, templatedBaseLink);
    }

    public AbstractResource(Link templatedBaseLink, Link self) {
        Preconditions.checkNotNull(self);
        Preconditions.checkNotNull(templatedBaseLink);
        this.self = self;
        this.templatedBaseLink = templatedBaseLink;
    }

    protected abstract ParameterizedTypeReference<T> getParameterizedTypeReference();

    protected abstract Class<T> getResourceType();

    protected final T getApiData() {
        return data;
    }

    protected final Link getProjectionLink(Link targetLink, String projection) {
        return targetLink.expand(Collections.singletonMap(RestApiConstants.PROJECTION, projection));
    }

    protected final ResponseEntity<T> getResponseEntity() throws RestClientException {
        loadIfNecessary();
        return responseEntity;
    }

    private void loadIfNecessary() {
        if (responseEntity == null) {
            responseEntity = restTemplate.exchange(
                    self.expand().getHref(),
                    HttpMethod.GET,
                    entityForHALData,
                    getParameterizedTypeReference());
        }
    }

    protected final Link getLinkFor(ResponseEntity<? extends AbstractBaseApiData<?>> responseEntity, String rel) {
        Optional<Link> linkOptional = responseEntity.getBody().getLinks()
                .stream()
                .filter(link -> link.getRel().equals(rel))
                .findAny();

        return linkOptional.orElse(null);
    }

    protected final ResponseEntity<T> createInternal(T entity) throws RestClientException {
        return restTemplate.exchange(
                self.expand().getHref(),
                HttpMethod.POST,
                new HttpEntity<>(entity, HEADERS_FOR_HAL_DATA),
                getParameterizedTypeReference());
    }

    public T read() throws RestClientException {
        return getResponseEntity().getBody();
    }

    protected final void updateInternal(T entity) throws RestClientException {
        restTemplate.put(entity.getSelf().expand().getHref(), entity);
    }

    protected final void updateInternal(Link target, T entity) throws RestClientException {
        restTemplate.put(target.expand().getHref(), entity);
    }

    protected final void deleteInternal(T entity) throws RestClientException {
        restTemplate.delete(entity.getSelf().expand().getHref());
    }
}
