package com.cargo.booking.starter.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
public class JwtUtils {

    private PublicKey publicKey;

    private static final String PARTICIPANT = "prt";
    private static final String WORKSPACE_ROLE = "wsr";
    private static final String AUTHORITIES = "auth";

    public JwtUtils(JwtProperties jwtProperties) {
        byte[] decoded = Base64.getDecoder().decode(jwtProperties.getPublicKey());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Ошибка при инициализации публичного ключа значением из конфига", e);
            publicKey = null;
        }
    }

    public String getJwtTokenSubject(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public JwtClaims getJwtTokenClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();
        Optional<UUID> participant = Optional.ofNullable(claims.get(PARTICIPANT)).map(p -> UUID.fromString((String) p));
        Optional<UUID> workspaceRole = Optional.ofNullable(claims.get(WORKSPACE_ROLE)).map(p -> UUID.fromString((String) p));
        List<String> authorities = (List<String>) claims.get(AUTHORITIES);
        return new JwtClaims(subject, participant, workspaceRole,
                authorities == null ? Collections.emptyList() : authorities, expiration);
    }
}
