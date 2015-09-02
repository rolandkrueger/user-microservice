package info.rolandkrueger.userservice.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Roland Krüger
 */
@Entity
public class Authority implements GrantedAuthority {
    private Long id;
    @Version
    Long version;
    @LastModifiedDate
    LocalDateTime lastModified;

    @NotBlank
    private String authority;

    private String description;

    private Authority() {
        lastModified = LocalDateTime.now();
        version = 1L;
    }

    public Authority(String authority) {
        this();
        setAuthority(authority);
    }

    public Authority(String authority, String description) {
        this(authority);
        this.description = description;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected void setAuthority(String authority) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(MoreObjects.firstNonNull(authority, "").trim()));
        this.authority = authority;
    }

    @Override
    @Column(unique = true)
    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Authority.class)
                .add("authority", authority)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Authority)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Authority other = (Authority) obj;
        return Objects.equals(authority, other.authority);
    }

    @Override
    public int hashCode() {
        return authority.hashCode();
    }
}
