package info.rolandkrueger.userservice.api.resources;

import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import org.springframework.hateoas.Link;

/**
 * @author Roland Krüger
 */
public class SearchResource extends AbstractRestClient {

    private Link searchLink;

    SearchResource(Link searchLink) {
        assert searchLink != null;
        this.searchLink = searchLink;
    }
}
