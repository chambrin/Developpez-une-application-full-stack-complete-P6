package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.AccountManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int PREFIX_LENGTH = 7;
    private static final int UNAUTHORIZED_STATUS = 401;

    @Autowired
    private AccountManager userManagementService;

    @Autowired
    private JWTService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, 
                                  HttpServletResponse httpResponse, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authToken = retrieveTokenFromRequest(httpRequest);
        
        if (authToken != null) {
            if (processAuthenticationToken(httpRequest, httpResponse, authToken)) {
                continueFilterChain(filterChain, httpRequest, httpResponse);
                return;
            } else {
                return; // Authentication failed, response already set
            }
        }
        
        continueFilterChain(filterChain, httpRequest, httpResponse);
    }

    private String retrieveTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(TOKEN_HEADER);
        
        if (headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(PREFIX_LENGTH);
        }
        
        return null;
    }

    private boolean processAuthenticationToken(HttpServletRequest request, 
                                             HttpServletResponse response, 
                                             String token) {
        try {
            Claims tokenClaims = extractClaimsFromToken(token);
            String userEmail = getUserEmailFromClaims(tokenClaims);
            
            if (isAuthenticationRequired()) {
                return authenticateUserIfValid(request, userEmail, tokenClaims);
            }
            
            return true;
        } catch (Exception exception) {
            handleAuthenticationError(response);
            return false;
        }
    }

    private Claims extractClaimsFromToken(String token) {
        return tokenService.decodeToken(token);
    }

    private String getUserEmailFromClaims(Claims claims) {
        return claims.getSubject();
    }

    private boolean isAuthenticationRequired() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean authenticateUserIfValid(HttpServletRequest request, 
                                          String email, 
                                          Claims claims) {
        Optional<User> userOptional = findUserByEmail(email);
        
        if (userOptional.isPresent()) {
            User authenticatedUser = userOptional.get();
            
            if (isTokenValidForUser(claims, authenticatedUser)) {
                setAuthenticationInContext(request, authenticatedUser);
                return true;
            }
        }
        
        return true; // Continue even if user not found or token invalid
    }

    private Optional<User> findUserByEmail(String email) {
        return userManagementService.getUserByEmail(email);
    }

    private boolean isTokenValidForUser(Claims claims, User user) {
        return verifyTokenOwnership(claims, user) && verifyTokenNotExpired(claims);
    }

    private boolean verifyTokenOwnership(Claims claims, User user) {
        String tokenEmail = claims.getSubject();
        return tokenEmail != null && tokenEmail.equals(user.getEmail());
    }

    private boolean verifyTokenNotExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        Date currentDate = new Date();
        return !expirationDate.before(currentDate);
    }

    private void setAuthenticationInContext(HttpServletRequest request, User user) {
        UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(user);
        attachRequestDetails(authToken, request);
        updateSecurityContext(authToken);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(User user) {
        return new UsernamePasswordAuthenticationToken(
            user, 
            null, 
            new ArrayList<>()
        );
    }

    private void attachRequestDetails(UsernamePasswordAuthenticationToken authToken, 
                                    HttpServletRequest request) {
        WebAuthenticationDetailsSource detailsSource = new WebAuthenticationDetailsSource();
        authToken.setDetails(detailsSource.buildDetails(request));
    }

    private void updateSecurityContext(UsernamePasswordAuthenticationToken authToken) {
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleAuthenticationError(HttpServletResponse response) {
        response.setStatus(UNAUTHORIZED_STATUS);
    }

    private void continueFilterChain(FilterChain chain, 
                                   HttpServletRequest request, 
                                   HttpServletResponse response) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}