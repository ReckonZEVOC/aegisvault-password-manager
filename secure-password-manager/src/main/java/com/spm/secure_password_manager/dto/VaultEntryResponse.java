package com.spm.secure_password_manager.dto;

public class VaultEntryResponse {

    private Long id;
    private String siteName;
    private String siteUsername;
    private String password;

    public VaultEntryResponse(Long id, String siteName,
                              String siteUsername, String password) {
        this.id = id;
        this.siteName = siteName;
        this.siteUsername = siteUsername;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteUsername() {
        return siteUsername;
    }

    public String getPassword() {
        return password;
    }
}
