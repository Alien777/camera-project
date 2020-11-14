package pl.lasota.crm.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lasota.crm.enums.PrivilegeName;
import pl.lasota.crm.enums.RoleName;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@ToString()
public final class PrincipalUser implements UserDetails {
    private final String identifier;
    private final String email;
    private final String password;
    private final boolean active;
    private final Set<PrivilegeName> privilege;
    private final Set<RoleName> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return privilege.stream().map(s -> (GrantedAuthority) s::toString).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return identifier;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
