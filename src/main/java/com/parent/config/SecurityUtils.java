package com.parent.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static Long getLoggedInOwnerId() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return null;
		HttpServletRequest req = attrs.getRequest();
		Object ownerId = req.getAttribute("ownerId");
		if (ownerId == null)
			return null;
		try {
			return Long.valueOf(String.valueOf(ownerId));
		} catch (Exception e) {
			return null;
		}
	}

	public static String getLoggedInEmail() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return null;
		return String.valueOf(auth.getPrincipal());
	}

	public static Long getLoggedInStaffId() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return null;
		HttpServletRequest req = attrs.getRequest();
		Object id = req.getAttribute("staffId");
		return id == null ? null : Long.valueOf(String.valueOf(id));
	}

	public static Long getStaffPgId() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return null;
		HttpServletRequest req = attrs.getRequest();
		Object id = req.getAttribute("pgId");
		return id == null ? null : Long.valueOf(String.valueOf(id));
	}

}
