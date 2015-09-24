package info.rolandkrueger.userservice.api.model;

import com.google.common.base.MoreObjects;
import info.rolandkrueger.userservice.api._internal.AbstractResource;
import info.rolandkrueger.userservice.api._internal.model.AbstractBaseApiData;
import info.rolandkrueger.userservice.api.resources.AuthorityResource;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;

/**
 * @author Roland Krüger
 */
public class AuthorityApiData extends AbstractBaseApiData<AuthorityResource> {
    private Long version;
    private LocalDateTime lastModified;
    private String authority;
    private String description;

    public AuthorityApiData() {
    }

    public AuthorityApiData(String authority, String description) {
        setAuthority(authority);
        setDescription(description);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(AuthorityApiData.class)
                .add("authority", authority)
                .add("description", description)
                .toString();
    }

    @Override
    protected AuthorityResource createNewResource(Link self) {
        return new AuthorityResource(self, this);
    }
}
