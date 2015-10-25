package info.rolandkrueger.userservice.api._internal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import org.springframework.hateoas.Link;

import java.util.Collection;

/**
 * Client-side data model of the service. Each resource of a RESTful API provides an interface to one particular data
 * type. On the client-side, this data-type is represented by sub-classes of {@link AbstractBaseApiData}. These
 * sub-classes add individual data fields which represent the data provided by the resource.
 * <p/>
 * This class provides general functionality that is necessary to interact with a resource from the client.
 *
 * @param <R> resource type which corresponds to this data model
 * @author Roland Krüger
 */
public abstract class AbstractBaseApiData<R extends AbstractResource> {
    private Collection<Link> links;

    /**
     * Creates a new resource object for this data type. The method uses the self link to create the resource.
     *
     * @param self link to the resource
     * @return a new resource object that represents this data type on the server side
     * @see #getSelf()
     */
    protected abstract R createNewResource(Link self);

    /**
     * Provides a collection of all resource links that could be read from the resource. These links lead to related
     * resources and can be used to create new instances of subclasses of {@link AbstractResource} or
     * {@link info.rolandkrueger.userservice.api._internal.AbstractPagedResource}.
     *
     * @return the collection of resource links
     */
    public final Collection<Link> getLinks() {
        return links;
    }

    /**
     * Sets the list of the available links to related resources. Will be called by the JSON unmarshaller.
     *
     * @param links collection of resource links
     */
    public final void setLinks(Collection<Link> links) {
        this.links = links;
    }

    /**
     * Provides the link to self.
     *
     * @return the self link
     */
    public final Link getSelf() {
        return links == null ? null : links.iterator().next();
    }

    /**
     * Provides the resource object that corresponds to this data type.
     * <p/>
     * Creates a new instance of {@link <R>} on every invocation. Is ignored by the JSON processor when transferring an
     * {@link AbstractBaseApiData} to the server.
     *
     * @return a new instance of a corresponding sub-class of {@link AbstractResource} for this data type.
     */
    @JsonIgnore
    public final R getResource() {
        R resource = createNewResource(getSelf());
        return resource;
    }
}
