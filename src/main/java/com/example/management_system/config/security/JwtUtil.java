package com.example.management_system.config.security;


import com.example.management_system.controller.UserController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JwtUtil {
    private static final String SECRET_KEY = "cLUGvOYD9P2C6C8eDQSoQq64EOO+ON2hVV2tN5TcrEoXX4tUYdBojW9aJmE0PSfESYt+o6WdnvU1tbVzTUBF+w==";
    private static final String AUTHORITIES_KEY = "auth";

    public static final long TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(120);

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    public String createToken(String username, Set<String> authorities) {
        long now = new Date().getTime();

        LOGGER.info(String.join("", authorities));
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, String.join("", authorities))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setExpiration(new Date(now + TOKEN_VALIDITY))
                .compact();
    }

    public UserPrincipal getUserDetails(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        Set<String> authorities = new HashSet<>();
        authorities.add(claims.get("auth").toString());
        return new UserPrincipal(claims.getSubject(), authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
