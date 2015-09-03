package info.rolandkrueger.userservice.api._internal;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

/**
 * @author Roland Krüger
 */
public abstract class AbstractResource<T extends BaseApiData> extends AbstractRestClient {

    protected Link self;

    public AbstractResource(Link self) {
        Preconditions.checkNotNull(self);
        this.self = self;
    }

    protected abstract Class<T> getResourceType();

    public final ResponseEntity<T> create(T entity) throws RestClientException {
        return restTemplate.postForEntity(self.expand().getHref(), entity, getResourceType());
    }

    public final void delete(T entity) throws RestClientException {
        restTemplate.delete(entity.getSelf().expand().getHref());
    }
}
