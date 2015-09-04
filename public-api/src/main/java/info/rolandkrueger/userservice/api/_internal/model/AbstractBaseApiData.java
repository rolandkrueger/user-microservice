package info.rolandkrueger.userservice.api._internal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import org.springframework.hateoas.Link;

import java.util.Collection;

/**
 * @author Roland Krüger
 */
public abstract class AbstractBaseApiData<R extends AbstractResource> {
    private Collection<Link> links;

    protected abstract R createNewResource(Link self);

    public final Collection<Link> getLinks() {
        return links;
    }

    public final void setLinks(Collection<Link> links) {
        this.links = links;
    }

    public final Link getSelf() {
        return links == null ? null : links.iterator().next();
    }

    @JsonIgnore
    public final R getResource() {
        R resource = createNewResource(getSelf());
        return resource;
    }
}
