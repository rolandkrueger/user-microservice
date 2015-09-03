package info.rolandkrueger.userservice.api._internal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Link;

import java.util.Collection;

/**
 * @author Roland Krüger
 */
public class BaseApiData {
    private Collection<Link> links;

    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }

    public Link getSelf() {
        return links == null ? null : links.iterator().next();
    }
}
