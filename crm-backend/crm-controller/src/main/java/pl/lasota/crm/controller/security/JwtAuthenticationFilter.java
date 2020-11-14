package pl.lasota.crm.controller.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtProvider tokenProvider;
    private final List<String> anonymousPath;

    private final CacheUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(CacheUserDetailsService customUserDetailsService, JwtProvider tokenProvider, List<String> anonymousPath) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenProvider = tokenProvider;
        this.anonymousPath = anonymousPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (anonymousPath.stream().anyMatch(path::contains)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);
            String userAccountId = tokenProvider.getUserAccountId(jwt);
            PrincipalUser userDetails = (PrincipalUser) customUserDetailsService.loadUserBYUserNameFromCache(userAccountId);
            if (userDetails == null) {
                return;
            }

            String newToken = customUserDetailsService.refreshPrincipalUser(userAccountId);
            if (newToken == null) {
                return;
            }
            response.addHeader("newToken", newToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            logger.error("Could not set user authentication in pl.lasota.crm.security context", ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        return request.getHeader("authorization");
    }
}
