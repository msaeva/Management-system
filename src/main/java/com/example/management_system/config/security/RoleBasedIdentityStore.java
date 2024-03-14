package com.example.management_system.config.security;

import com.example.management_system.domain.entity.User;
import com.example.management_system.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class RoleBasedIdentityStore implements IdentityStore {

    @Inject
    private UserService userService;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Set<String> result = new HashSet<>();
        String username = validationResult.getCallerPrincipal().getName();
        Optional<User> currentUser = userService.findByUsername(username);
        currentUser.ifPresent(user -> result.add(user.getRole().getRole().name()));
        return result;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.PROVIDE_GROUPS);
    }
}
