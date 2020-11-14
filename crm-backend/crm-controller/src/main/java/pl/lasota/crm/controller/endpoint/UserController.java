package pl.lasota.crm.controller.endpoint;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lasota.crm.controller.security.JwtProvider;
import pl.lasota.crm.controller.security.SecureString;
import pl.lasota.crm.controller.security.Secured;
import pl.lasota.crm.domain.UserDomain;
import pl.lasota.crm.dto.user.*;
import pl.lasota.crm.enums.PrivilegeName;
import pl.lasota.crm.enums.RoleName;
import pl.lasota.crm.service.user.IUserService;
import pl.lasota.tool.sr.service.security.ProvidingContext;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService iUserService;

    private final PasswordEncoder passwordEncoder;

    private final ProvidingContext context;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public UserController(IUserService iUserService, PasswordEncoder passwordEncoder, ProvidingContext context, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {

        this.iUserService = iUserService;
        this.passwordEncoder = passwordEncoder;
        this.context = context;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public void register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        iUserService.create(userRegisterDto.getName(), userRegisterDto.getEmail(), passwordEncoder.encode(new SecureString(userRegisterDto.getPass())), RoleName.USER);
    }

    @GetMapping("/activate/{activator}")
    @PreAuthorize("isAnonymous()")
    public void activate(@PathVariable(name = "activator") UUID activator) {
        iUserService.activate(activator);
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public UserLogged login(@RequestBody @Valid UserLoginDto userLoginDto) {
        if (userLoginDto.getTypeClient() == TypeClient.WEB) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            new SecureString(userLoginDto.getPass()))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.createToken(authentication);
            UserDomain userDomain = iUserService.get(userLoginDto.getEmail());

            return new UserLogged(userDomain.getIdentifier(), UUID.randomUUID().toString(), token);
        } else {
            UserDomain userDomain = iUserService.get(userLoginDto.getEmail());
            String token = jwtProvider.createToken(userDomain.getIdentifier());
            return new UserLogged(userDomain.getIdentifier(), UUID.randomUUID().toString(), token);
        }

    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public boolean logout() {
        return true;
    }

    @PostMapping("/delete")
    @Secured({PrivilegeName.MANAGE_OWN_ACCOUNT})
    public void delete() {
        iUserService.delete(UUID.fromString(context.supply().getOwner()));
    }

    @PostMapping("/get")
    @Secured({PrivilegeName.MANAGE_OWN_ACCOUNT})
    public UserReadDto get() {
        return iUserService.get(UUID.fromString(context.supply().getOwner()));
    }

}
