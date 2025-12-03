package com.parent.admin.dto;

import java.util.Set;

public class UpdateAdminRequest {
    public String name;
    public Set<String> permissions;
    public Set<Long> allowedPgIds;
    
    
}
