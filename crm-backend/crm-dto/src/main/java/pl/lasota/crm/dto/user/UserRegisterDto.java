package pl.lasota.crm.dto.user;

import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserRegisterDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private char[] pass;
}

