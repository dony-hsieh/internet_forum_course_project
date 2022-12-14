package com.fcu.security;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 7200000;  // 2 hours
    private static final String ISSUER = "FCU_QAFORUM_112";
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // generate secret key

    public String generateToken(String username) {
        long curTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(curTimeMillis))
                .setExpiration(new Date(curTimeMillis + EXPIRATION_TIME))
                .setIssuer(ISSUER)
                .signWith(key)
                .compact();
    }

    public String generateToken(String username, Key optionalKey) {
        if (optionalKey == null) {
            return generateToken(username);
        }
        long curTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(curTimeMillis))
                .setExpiration(new Date(curTimeMillis + EXPIRATION_TIME))
                .setIssuer(ISSUER)
                .signWith(optionalKey)
                .compact();
    }

    public String generateToken(String username, Key optionalKey, long expirationTime) {
        if (optionalKey == null) {
            return generateToken(username);
        }
        long curTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(curTimeMillis))
                .setExpiration(new Date(curTimeMillis + expirationTime))
                .setIssuer(ISSUER)
                .signWith(optionalKey)
                .compact();
    }

    public String resolveToken(String token) {
        Jws<Claims> jws;
        Claims claims;
        try {
            jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            claims = jws.getBody();
            return claims.getSubject();
        } catch (MissingClaimException mce) {
            System.out.println("Missing claim exception.");
        } catch (IncorrectClaimException ice) {
            System.out.println("Incorrect claim exception.");
        } catch (ExpiredJwtException eje) {
            System.out.println("Expired jwt exception.");
        } catch (SignatureException se) {
            System.out.println("Signature exception.");
        }
        return null;
    }

    public String resolveToken(String token, Key optionalKey) {
        if (optionalKey == null) {
            return resolveToken(token);
        }
        Jws<Claims> jws;
        Claims claims;
        try {
            jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(optionalKey)
                    .build()
                    .parseClaimsJws(token);
            claims = jws.getBody();
            return claims.getSubject();
        } catch (MissingClaimException mce) {
            System.out.println("Missing claim exception.");
        } catch (IncorrectClaimException ice) {
            System.out.println("Incorrect claim exception.");
        } catch (ExpiredJwtException eje) {
            System.out.println("Expired jwt exception.");
        } catch (SignatureException se) {
            System.out.println("Signature exception.");
        }
        return null;
    }
}
