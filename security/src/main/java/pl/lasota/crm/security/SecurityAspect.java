package pl.lasota.crm.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lasota.crm.enums.PrivilegeName;

import java.util.Arrays;
import java.util.List;

@Aspect
public class SecurityAspect {

    @Before("execution(* pl.lasota.crm.controller.endpoint.*(..))  && @annotation(secured)")
    public void checkPrivilege(JoinPoint joinPoint, Secured secured) throws Exception {
        List<PrivilegeName> privilegeNames = Arrays.asList(secured.value());
        PrincipalUser principal = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean b = principal.getPrivilege().stream().anyMatch(privilegeNames::contains);
        if (!b) {
            throw new Exception("Not privileges");
        }
    }
}
