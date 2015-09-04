package info.rolandkrueger.userservice.api._internal;

import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.model.BaseApiData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class SearchResource extends AbstractResource<BaseApiData> {

    public SearchResource(Link self) {
        super(self);
    }

    @Override
    protected ParameterizedTypeReference<BaseApiData> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<BaseApiData>() {
        };
    }

    @Override
    protected Class<BaseApiData> getResourceType() {
        return BaseApiData.class;
    }
}
