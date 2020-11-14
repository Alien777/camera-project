package pl.lasota.crm.domain;

import lombok.Data;
import pl.lasota.crm.enums.PrivilegeName;
import pl.lasota.crm.enums.RoleName;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDomain {
    Set<RoleName> roles;

    Set<PrivilegeName> privileges;

    String password;

    String email;

    UUID identifier;

    boolean isActive;

}
