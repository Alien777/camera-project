package pl.lasota.crm.controller.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private static final String PRIVILEGES_CLAIM = "privileges";
    private final byte[] key;
    private final long expiration;

    public JwtProvider(byte[] key, long expiration) {
        this.key = key;
        this.expiration = expiration;
    }


    public final String createToken(UUID userId) {

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public final String createToken(Authentication authentication) {

        PrincipalUser principal = (PrincipalUser) authentication.getPrincipal();
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(principal.getIdentifier())
                .claim(PRIVILEGES_CLAIM, principal.getPrivilege())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public final String createToken(PrincipalUser principal) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(principal.getIdentifier())
                .claim(PRIVILEGES_CLAIM, principal.getPrivilege())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getUserAccountId(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return "isNotValid";
    }
}

