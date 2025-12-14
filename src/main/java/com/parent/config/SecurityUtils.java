package com.parent.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

public final class SecurityUtils {

    private SecurityUtils() {}

    // ------------------------------------------------------------
    // INTERNAL UTILITY: Get current HttpServletRequest
    // ------------------------------------------------------------
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attrs == null) ? null : attrs.getRequest();
    }

    // ------------------------------------------------------------
    // ✔ OWNER ID
    // ------------------------------------------------------------
    public static Long getLoggedInOwnerId() {
        HttpServletRequest req = getRequest();
        if (req == null) return null;

        Object ownerId = req.getAttribute("ownerId");
        return ownerId == null ? null : Long.valueOf(String.valueOf(ownerId));
    }

    // ------------------------------------------------------------
    // ✔ MANAGER ID
    // ------------------------------------------------------------
    public static Long getLoggedInManagerId() {
        HttpServletRequest req = getRequest();
        if (req == null) return null;

        Object managerId = req.getAttribute("managerId");
        return managerId == null ? null : Long.valueOf(String.valueOf(managerId));
    }

    // ------------------------------------------------------------
    // ✔ ADMIN ID (NEW)
    // ------------------------------------------------------------
    public static Long getLoggedInAdminId() {
        HttpServletRequest req = getRequest();
        if (req == null) return null;

        Object adminId = req.getAttribute("adminId");
        return adminId == null ? null : Long.valueOf(String.valueOf(adminId));
    }

    // ------------------------------------------------------------
    // ✔ GET ADMIN PERMISSIONS (NEW)
    // ------------------------------------------------------------
    public static Set<String> getLoggedInPermissions() {
        HttpServletRequest req = getRequest();
        if (req == null) return Set.of();

        Object perms = req.getAttribute("permissions");
        return perms == null ? Set.of() : (Set<String>) perms;
    }

    // ------------------------------------------------------------
    // ✔ PGs allowed for MANAGER or ADMIN (NEW)
    // ------------------------------------------------------------
    public static Set<Long> getAllowedPgIds() {
        HttpServletRequest req = getRequest();
        if (req == null) return Set.of();

        Object ids = req.getAttribute("allowedPgIds");
        return ids == null ? Set.of() : (Set<Long>) ids;
    }

    // ------------------------------------------------------------
    // ✔ Generic attribute accessor (NEW)
    // Used by TenantServiceImpl to get additional attributes
    // ------------------------------------------------------------
    public static Object getRequestAttribute(String key) {
        HttpServletRequest req = getRequest();
        return (req == null) ? null : req.getAttribute(key);
    }

    // ------------------------------------------------------------
    // ✔ EMAIL / USERNAME from Authentication
    // ------------------------------------------------------------
    public static String getLoggedInEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null) ? null : String.valueOf(auth.getPrincipal());
    }
}
