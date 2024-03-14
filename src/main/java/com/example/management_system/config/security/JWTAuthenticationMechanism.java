package com.example.management_system.config.security;

import com.example.management_system.controller.UserController;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

@RequestScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Inject
    private JwtUtil jwtUtil;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String token = extractToken(context);

        if (username != null && password != null) {
            CredentialValidationResult result = identityStoreHandler.validate(new UsernamePasswordCredential(username, password));
            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                return createToken(result, context);
            }
            return context.responseUnauthorized();
        } else if (token != null) {
            AuthenticationStatus validatedToken = validateToken(token, context);
            LOGGER.info(validatedToken.toString());
            return validatedToken;
        } else if (context.isProtected()) {
            return context.responseUnauthorized();
        }

        return context.doNothing();
    }

    private AuthenticationStatus validateToken(String token, HttpMessageContext context) {

        LOGGER.info("token " + token);
        try {
            if (jwtUtil.validateToken(token)) {
                UserPrincipal userPrincipal = jwtUtil.getUserDetails(token);
                LOGGER.info("info " + userPrincipal.getPrincipal());
                LOGGER.info("info " + userPrincipal.getAuthorities());
                return context.notifyContainerAboutLogin(userPrincipal.getPrincipal(), userPrincipal.getAuthorities());
            }
            LOGGER.info("inside if ");
            return context.responseUnauthorized();
        } catch (ExpiredJwtException e) {
            LOGGER.info("in catch block ");
            return context.responseUnauthorized();
        }
    }

    private AuthenticationStatus createToken(CredentialValidationResult result, HttpMessageContext context) {
        String jwt = jwtUtil.createToken(result.getCallerPrincipal().getName(), result.getCallerGroups());
        context.getResponse().setHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
        return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
    }

    private String extractToken(HttpMessageContext context) {
        String authorizationHeader = context.getRequest().getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer".length(), authorizationHeader.length());
            return token;
        }
        return null;
    }
}
