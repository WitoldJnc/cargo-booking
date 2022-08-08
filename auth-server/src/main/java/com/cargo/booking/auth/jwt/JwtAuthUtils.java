package com.cargo.booking.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JwtAuthUtils {

    @Value("${jwt.privateKey}")
    private String base64PrivateKey;

    private PrivateKey privateKey;

    private static final String PARTICIPANT = "prt";
    private static final String WORKSPACE_ROLE = "wsr";
    private static final String AUTHORITIES = "auth";

    @PostConstruct
    public void initPrivateKey() {
        byte[] decoded = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Ошибка при инициализации приватного ключа значением из конфига", e);
            privateKey = null;
        }
    }

    public String generateAccessToken(String username, Set<String> authorities, int tokenLifeTime) {
        Date expiration = Date.from(LocalDateTime.now().plusSeconds(tokenLifeTime).atZone(ZoneId.systemDefault()).toInstant());
        return generateAccessToken(username, Optional.empty(),  Optional.empty(), authorities, expiration);
    }

    public String generateAccessToken(String username, Optional<UUID> participantId,
                                      Optional<UUID> workspaceRoleId, Set<String> authorities,
                                      Date expiration) {
        Map<String, Object> params = new HashMap<>();
        participantId.ifPresent(pId -> params.put(PARTICIPANT, pId));
        workspaceRoleId.ifPresent(wrId -> params.put(WORKSPACE_ROLE, wrId));
        params.put(AUTHORITIES, authorities);
        return Jwts.builder()
                .setClaims(params)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(privateKey)
                .compact();
    }

    /**
     * validate token
     *
     * @param token token
     * @return whether valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException | SignatureException e) {
            log.debug("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        }
        return false;
    }
}
