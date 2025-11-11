package com.parent.pg.dto;

public class AmenityResponse {
    private Long id;
    private String name;
    private Long pgId;

    public AmenityResponse() {}

    public AmenityResponse(Long id, String name, Long pgId) {
        this.id = id;
        this.name = name;
        this.pgId = pgId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getPgId() { return pgId; }
    public void setPgId(Long pgId) { this.pgId = pgId; }
}
