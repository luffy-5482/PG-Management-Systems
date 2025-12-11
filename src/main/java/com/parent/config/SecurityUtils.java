package com.parent.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Long getLoggedInOwnerId() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest req = attrs.getRequest();
        Object ownerId = req.getAttribute("ownerId");
        return ownerId == null ? null : Long.valueOf(String.valueOf(ownerId));
    }

    public static Long getLoggedInManagerId() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest req = attrs.getRequest();
        Object managerId = req.getAttribute("managerId");
        return managerId == null ? null : Long.valueOf(String.valueOf(managerId));
    }

    public static String getLoggedInEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : String.valueOf(auth.getPrincipal());
    }
    
}
