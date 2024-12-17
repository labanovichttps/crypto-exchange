package com.labanovich.crypto.exchange.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@UtilityClass
public class SecurityUtil {

    public String getCurrentUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .map(it -> {
                if (it instanceof UserDetails userDetails) {
                    return userDetails.getUsername();
                }
                return null;
            }).orElse(null);
    }
}
