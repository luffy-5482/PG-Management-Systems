package com.parent.pg.dto;

public class AmenityRequest {
    private String name;
    private Long pgId;

    public AmenityRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getPgId() { return pgId; }
    public void setPgId(Long pgId) { this.pgId = pgId; }
}
