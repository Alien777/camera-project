package pl.lasota.crm.common.endpoint.clienttomanage;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
public class Credential {
    public static final String CLIENT_LOGIN_TO_MANAGER = "login-client-endpoint";

}
