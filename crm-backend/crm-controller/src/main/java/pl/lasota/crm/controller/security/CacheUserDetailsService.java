package pl.lasota.crm.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.lasota.crm.domain.UserDomain;
import pl.lasota.crm.service.user.IUserService;
import pl.lasota.tool.cache.Cache;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CacheUserDetailsService implements UserDetailsService {

    private final Cache<String, PrincipalUser> cacheControl;
    private final IUserService userService;
    private final JwtProvider tokenProvider;

    public CacheUserDetailsService(Cache<String, PrincipalUser> cacheControl, IUserService userService, JwtProvider tokenProvider) {
        this.cacheControl = cacheControl;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrIdentifier) throws UsernameNotFoundException {

        UserDomain user;
        try {
            user = userService.get(emailOrIdentifier);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }

        UUID identifier = user.getIdentifier();

        PrincipalUser principalUser = new PrincipalUser(identifier.toString(), user.getEmail(),
                user.getPassword(), user.isActive(), user.getPrivileges(), user.getRoles());
        log.info(principalUser.toString());
        cacheControl.add(identifier.toString(), principalUser);

        return principalUser;
    }


    public UserDetails loadUserBYUserNameFromCache(String identifier) {
        Optional<PrincipalUser> principalUser = cacheControl.get(identifier);
        return principalUser.orElse(null);
    }

    public String refreshPrincipalUser(String identifier) {
        Optional<PrincipalUser> principalUser = cacheControl.get(identifier);
        if (principalUser.isPresent()) {
            PrincipalUser principalUser1 = principalUser.get();
            cacheControl.add(principalUser1.getIdentifier(), principalUser1);
            return tokenProvider.createToken(principalUser1);
        }
        return null;
    }
}
