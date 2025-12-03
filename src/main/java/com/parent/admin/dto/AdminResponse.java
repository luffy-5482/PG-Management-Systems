package com.parent.admin.dto;

import java.time.Instant;
import java.util.Set;

public class AdminResponse {
    public Long id;
    public String name;
    public String email;
    public Set<String> permissions;
    public Set<Long> allowedPgIds;
    public Instant createdAt;
}
