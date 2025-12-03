package com.parent.admin.dto;

import java.util.Set;

public class CreateAdminRequest {
    public String name;
    public String email;
    public String password;
    public Set<String> permissions;
    public Set<Long> allowedPgIds;
}
