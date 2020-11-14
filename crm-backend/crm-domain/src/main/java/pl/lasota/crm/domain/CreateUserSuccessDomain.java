package pl.lasota.crm.domain;


import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserSuccessDomain {
    private final UUID activator;
    private final String name;
    private final String email;

    public CreateUserSuccessDomain(UUID activator, String name, String email) {
        this.activator = activator;
        this.name = name;
        this.email = email;
    }
}
