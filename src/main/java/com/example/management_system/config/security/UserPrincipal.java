package com.example.management_system.config.security;

import jakarta.security.enterprise.credential.Credential;

import java.util.Set;

public class UserPrincipal implements Credential {
    private String principal;
    private Set<String> authorities;

    public UserPrincipal(String username, Set<String> authorities) {
        this.principal = username;
        this.authorities = authorities;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
