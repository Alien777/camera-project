package pl.lasota.crm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lasota.tool.cache.Cache;
import pl.lasota.tool.cache.CacheControl;
import pl.lasota.tool.cache.ExpiredCache;
import pl.lasota.tool.sr.service.security.Context;
import pl.lasota.tool.sr.service.security.ProvidingContext;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.*;

@Component
public class CryptoSecurity {

    public CryptoSecurity() {
        CacheControl
                .instance()
                .addCache("userCache", new ExpiredCache<String, PrincipalUser>(expirationKey(), TimeUnit.MILLISECONDS));
    }

    @Bean
    public byte[] createKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    @Bean
    public long expirationKey() {
        return 1800000;
    }

    @Bean
    @Scope("singleton")
    @SuppressWarnings("unchecked")
    public Cache<String, PrincipalUser> userCache() {
        return CacheControl.instance().getCache("userCache");
    }

    @Bean
    public ProvidingContext providingContext() {
        return () -> {
            PrincipalUser principal = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Context(principal.getIdentifier());
        };
    }

    @Bean
    List<String> anonymousPath() {
        String[] strings = new String[4];
        strings[0] = "/api/user/register";
        strings[1] = "/api/user/login";
        strings[2] = "/api/user/activate/**";
        strings[3] = "/api/user/activate";
        return Arrays.asList(strings);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
