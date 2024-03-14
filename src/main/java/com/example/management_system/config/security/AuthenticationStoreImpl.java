package com.example.management_system.config.security;

import com.example.management_system.domain.entity.User;
import com.example.management_system.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class AuthenticationStoreImpl implements IdentityStore {
    @Inject
    private UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CredentialValidationResult result;
        UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
        Optional<User> expectedUser = userService.findByUsername(usernamePassword.getCaller());
        if (expectedUser.isPresent() &&
                userService.checkPassword(usernamePassword.getPasswordAsString(), expectedUser.get().getPassword())) {
            result = new CredentialValidationResult(usernamePassword.getCaller());
        } else {
            result = CredentialValidationResult.INVALID_RESULT;
        }

        return result;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.VALIDATE);
    }
}
