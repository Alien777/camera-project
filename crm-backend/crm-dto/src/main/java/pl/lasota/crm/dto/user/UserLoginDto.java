package pl.lasota.crm.dto.user;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserLoginDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    private char[] pass;

    private TypeClient typeClient;
}
