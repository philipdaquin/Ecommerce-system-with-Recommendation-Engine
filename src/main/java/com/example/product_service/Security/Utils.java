package com.example.product_service.Security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Utils {

    /**
     * Gets the current user -get the currently authenticated principal 
     * @return
     */
    public static Optional<String> getCurrentUser() { 
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }
    /**
     * 
     * @param authentication
     * @return
     */
    public static String extractPrincipal(Authentication authentication) { 
        if (authentication == null) return null;
        else if (authentication.getPrincipal() instanceof UserDetails) { 
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) { 
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    
}
