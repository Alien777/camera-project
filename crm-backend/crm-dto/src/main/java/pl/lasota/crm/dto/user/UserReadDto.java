package pl.lasota.crm.dto.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserReadDto {

    private String name;

    private String email;

    private LocalDateTime createAnAccountTime;

    private String country;

    private boolean active;

    private Set<String> privileges;

    private Set<String> roles;

}
