package com.example.management_system.config.security;


import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.domain.entity.User;
import com.example.management_system.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JwtUtil {
    private static final String SECRET_KEY = "cLUGvOYD9P2C6C8eDQSoQq64EOO+ON2hVV2tN5TcrEoXX4tUYdBojW9aJmE0PSfESYt+o6WdnvU1tbVzTUBF+w==";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID_KEY = "id";

    private static final long TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(120);

    @Inject
    private UserService userService;

    public String createToken(String username, Set<String> authorities) {
        long now = new Date().getTime();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidUserException("User with username: " + username + " not found!"));
        return Jwts.builder()
                .setSubject(username)
                .claim(USER_ID_KEY, user.getId())
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
