package info.rolandkrueger.userservice.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Roland Krüger
 */
@Entity
public class Authority implements GrantedAuthority {
    private Long id;
    @NotBlank
    private String authority;

    private String description;

    private Authority() {}

    public Authority(String authority) {
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
    @JsonView(UserWithoutPasswordView.class)
    public String getAuthority() {
        return authority;
    }

    @JsonView(UserWithoutPasswordView.class)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return authority;
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
