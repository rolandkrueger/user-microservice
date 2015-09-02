package info.rolandkrueger.userservice.api.model;

import java.time.LocalDateTime;

/**
 * @author Roland Krüger
 */
public class AuthorityDTO {
    private Long version;
    private LocalDateTime lastModified;
    private String authority;
    private String description;

    public AuthorityDTO() {
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
}
