package pl.lasota.crm.service.user;

import org.springframework.data.domain.Page;
import pl.lasota.crm.domain.CreateUserSuccessDomain;
import pl.lasota.crm.domain.UserDomain;
import pl.lasota.crm.dto.Field;
import pl.lasota.crm.dto.user.UserReadDto;
import pl.lasota.crm.enums.RoleName;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    CreateUserSuccessDomain create(String name, String email, String password, RoleName roleName);

    void activate(UUID activator);

    void update(UUID identifier, Field field);

    void delete(UUID identifier);

    UserReadDto get(UUID identifier);

    UserDomain get(String emailOrIdentifier);

    Page<UserReadDto> search(List<Field> fields, int page, int size);

}
