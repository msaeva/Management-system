package com.example.management_system.config.security;

import jakarta.security.enterprise.credential.Credential;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserPrincipal implements Credential {
    private Long id;
    private String username;
    private Set<String> authorities;

    public UserPrincipal(Long id, String username, Set<String> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public UserPrincipal(String username, Set<String> authorities) {
        this.username = username;
        this.authorities = authorities;
    }
}
