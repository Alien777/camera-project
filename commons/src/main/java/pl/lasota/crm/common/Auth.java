package pl.lasota.crm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth {
    private String clientId;
    private UUID userId;
    private String token;
}
