package com.example.management_system.service;

import com.example.management_system.config.security.JwtUtil;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

import java.util.Set;

@Stateless
public class AuthService {
    @Inject
    private UserService userService;

    @Inject
    private UserRoleService userRoleService;

    @Inject
    private SecurityContext securityContext;
    @Inject
    private JwtUtil jwtUtil;

    public String getJwtToken(String username) {
        Set<String> authorities = userRoleService.findRoleByUsername(username);
        return jwtUtil.createToken(username, authorities);
    }

    public User getAuthenticatedUser() {
        if (securityContext.getCallerPrincipal() != null) {
            String name = securityContext.getCallerPrincipal().getName();
            return userService.findByUsername(name).orElse(null);
        }
        return null;
    }
}
