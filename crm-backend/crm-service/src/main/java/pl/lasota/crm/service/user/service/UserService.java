package pl.lasota.crm.service.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lasota.crm.domain.CreateUserSuccessDomain;
import pl.lasota.crm.domain.UserDomain;
import pl.lasota.crm.dto.Field;
import pl.lasota.crm.dto.user.UserReadDto;
import pl.lasota.crm.entity.user.Privilege;
import pl.lasota.crm.entity.user.Role;
import pl.lasota.crm.entity.user.Token;
import pl.lasota.crm.entity.user.User;
import pl.lasota.crm.enums.PrivilegeName;
import pl.lasota.crm.enums.RoleName;
import pl.lasota.crm.repository.user.RoleCrudAction;
import pl.lasota.crm.repository.user.TokenCrudAction;
import pl.lasota.crm.repository.user.UserCrudAction;
import pl.lasota.crm.service.user.IUserService;

import pl.lasota.tool.sr.repository.query.CriteriaBuilderImpl;
import pl.lasota.tool.sr.repository.query.Predicatable;
import pl.lasota.tool.sr.repository.query.QueryUpdate;
import pl.lasota.tool.sr.repository.query.Updatable;
import pl.lasota.tool.sr.repository.query.field.LikeField;
import pl.lasota.tool.sr.repository.query.normalize.LowerCaseNormalizer;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UserCrudAction userCrudAction;
    private final RoleCrudAction roleCrudAction;
    private final TokenCrudAction tokenCrudAction;

    public UserService(UserCrudAction userCrudAction, RoleCrudAction roleCrudAction, TokenCrudAction tokenCrudAction) {
        this.userCrudAction = userCrudAction;
        this.roleCrudAction = roleCrudAction;
        this.tokenCrudAction = tokenCrudAction;
    }

    @Override
    @Transactional
    public CreateUserSuccessDomain create(String name, String email, String password, RoleName roleName) {
        if (checkIfExisting(email)) {
            throw new IllegalStateException("Email is existing");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        UUID uuid = UUID.randomUUID();
        user.setIdentifier(uuid);
        user.setCreateAnAccountTime(LocalDateTime.now());
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable userRole = criteria.root(criteria.and(criteria.equals("name", roleName)));
        Pageable page = criteria.page(0, 1);
        Page<Role> roles = roleCrudAction.find(criteria.build(userRole, page));
        user.getRoles().add(roles.getContent().get(0));


        Token token = new Token();
        UUID activator = UUID.randomUUID();
        token.setActivator(activator);
        token.setUser(user);
        user.getTokens().add(token);

        userCrudAction.save(user);
        return new CreateUserSuccessDomain(activator, name, email);
    }

    @Override
    @Transactional
    public void activate(UUID activator) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable predicatable = criteria.root(criteria.and(criteria.equals("tokens.activator", activator)));
        Page<User> users = userCrudAction.find(criteria.build(predicatable, criteria.page(0, 1)));
        Long userId = users.getContent().get(0).getId();
        Predicatable predicatable1 = criteria.root(criteria.and(criteria.equals("id", userId)));
        Updatable updatable = criteria.set(criteria.update("active", true));
        QueryUpdate build = criteria.build(predicatable1, updatable);
        userCrudAction.update(build);
        Predicatable delete = criteria.root(criteria.and(criteria.equals("activator", activator)));
        tokenCrudAction.delete(criteria.build(delete));
    }

    @Override
    public UserDomain get(String emailOrIdentifier) {
        UUID uuid;
        try {
            uuid = UUID.fromString(emailOrIdentifier);
        } catch (Exception e) {
            uuid = UUID.randomUUID();
        }

        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable predicatable = criteria.root(
                criteria.or(criteria.equals("identifier", uuid))
                , criteria.or(criteria.equals("email", emailOrIdentifier)));
        Page<User> users = userCrudAction.find(criteria.build(predicatable, criteria.page(0, 1)));
        if (users.getContent().size() == 1) {
            User user = users.getContent().get(0);
            UserDomain userDomain = new UserDomain();
            userDomain.setActive(user.isActive());
            userDomain.setEmail(user.getEmail());
            userDomain.setPassword(user.getPassword());
            userDomain.setIdentifier(user.getIdentifier());
            userDomain.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
            userDomain.setActive(user.isActive());
            userDomain.setPrivileges(getAllPrivileges(user));

            return userDomain;
        }
        throw new IllegalStateException("userAccountId is probably not unique");
    }


    @Override
    public UserReadDto get(UUID identifier) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable predicatable = criteria.root(criteria.and(criteria.equals("identifier", identifier)));
        Page<User> users = userCrudAction.find(criteria.build(predicatable, criteria.page(0, 1)));

        User user = users.getContent().get(0);
        return map(user);
    }

    @Override
    public Page<UserReadDto> search(List<Field> fields, int page, int size) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();

        LikeField[] objects = fields
                .stream()
                .map(field -> criteria.like(field.getPath(),
                        field.getValue().toString().toLowerCase(), criteria.lowerCase())).toArray(LikeField[]::new);

        Predicatable predicatable = criteria.root(criteria.and(objects));
        Page<User> users = userCrudAction.find(criteria.build(predicatable, criteria.page(page, size)));
        return users.map(this::map);
    }

    @Override
    public void update(UUID identifier, Field field) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Updatable updatable = criteria.set(criteria.update(field.getPath(), field.getValue()));
        Predicatable predicatable = criteria.root(criteria.and(criteria.equals("identifier", identifier)));
        QueryUpdate build = criteria.build(predicatable, updatable);
        userCrudAction.update(build);
    }

    @Override
    public void delete(UUID identifier) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable predicatable = criteria.root(criteria.and(criteria.equals("identifier", identifier)));
        userCrudAction.delete(criteria.build(predicatable));
    }

    private boolean checkIfExisting(String email) {
        CriteriaBuilderImpl criteria = CriteriaBuilderImpl.criteria();
        Predicatable predicatable = criteria.root(criteria.and(criteria.equals("email", email.toLowerCase().trim(), new LowerCaseNormalizer())));
        Pageable page = criteria.page(0, 1);

        Page<User> users = userCrudAction.find(criteria.build(predicatable, page));
        return users.getContent().size() == 1;
    }

    private Set<PrivilegeName> getAllPrivileges(User user) {
        Set<PrivilegeName> privilegeNames = user.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toCollection(HashSet::new));
        Set<PrivilegeName> privilegeNamesFromRole = user.getRoles()
                .stream()
                .map(role -> role.getPrivileges()
                        .stream()
                        .map(Privilege::getName)
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        privilegeNames.addAll(privilegeNamesFromRole);

        return privilegeNames;
    }

    private UserReadDto map(User user) {
        UserReadDto userReadDto = new UserReadDto();
        userReadDto.setActive(user.isActive());
        userReadDto.setEmail(user.getEmail());
        userReadDto.setName(user.getName());
        userReadDto.setRoles(user.getRoles().stream().map(r -> r.getName().toString()).collect(Collectors.toSet()));
        userReadDto.setPrivileges(getAllPrivileges(user).stream().map(Enum::toString).collect(Collectors.toSet()));
        return userReadDto;
    }
}
