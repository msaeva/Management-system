package com.example.management_system.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String token = extractToken(context);

        if (request.getMethod().equals("OPTIONS")) {
            return context.doNothing();
        }

        if (username != null && password != null) {
            CredentialValidationResult result = identityStoreHandler.validate(new UsernamePasswordCredential(username, password));
            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                return createToken(result, context);
            }
            return context.responseUnauthorized();
        } else if (token != null) {
            return validateToken(token, context);
        } else if (context.isProtected()) {
            return context.responseUnauthorized();
        }

        return context.doNothing();
    }

    private AuthenticationStatus validateToken(String token, HttpMessageContext context) {
        try {
            if (jwtUtil.validateToken(token)) {
                UserPrincipal userPrincipal = jwtUtil.getUserDetails(token);
                return context.notifyContainerAboutLogin(userPrincipal.getUsername(), userPrincipal.getAuthorities());
            }
            addCorsHeaders(context.getResponse());
            return context.responseUnauthorized();
        } catch (ExpiredJwtException e) {
            return context.responseUnauthorized();
        }
    }

    private void addCorsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

    }

    private AuthenticationStatus createToken(CredentialValidationResult result, HttpMessageContext context) {
        String jwt = jwtUtil.createToken(result.getCallerPrincipal().getName(), result.getCallerGroups());
        context.getResponse().setHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
        return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
    }

    private String extractToken(HttpMessageContext context) {
        String authorizationHeader = context.getRequest().getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer".length(), authorizationHeader.length());
        }
        return null;
    }
}
