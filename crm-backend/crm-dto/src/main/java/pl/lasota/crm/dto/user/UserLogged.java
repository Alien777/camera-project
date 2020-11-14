package pl.lasota.crm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserLogged {
    private UUID userId;
    private String clientId;
    private String token;
}
