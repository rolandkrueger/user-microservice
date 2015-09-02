package info.rolandkrueger.userservice.api.model;

import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;

/**
 * @author Roland Krüger
 */
public class AuthorityApiData {
    private Long version;
    private LocalDateTime lastModified;
    private String authority;
    private String description;

    public AuthorityApiData() {
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
                .toString();
    }
}
