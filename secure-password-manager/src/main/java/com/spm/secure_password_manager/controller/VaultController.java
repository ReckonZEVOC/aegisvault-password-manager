package com.spm.secure_password_manager.controller;

import com.spm.secure_password_manager.dto.VaultEntryResponse;
import com.spm.secure_password_manager.service.VaultService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.spm.secure_password_manager.dto.AddPasswordRequest;

import java.util.List;

@RestController
@RequestMapping("/vault")
@CrossOrigin(origins = "*")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    // 🔐 Add password (requires account password)
    @PostMapping("/add")
    public String addPassword(@Valid @RequestBody AddPasswordRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return vaultService.addPassword(
                username,
                request.getAccountPassword(),
                request.getSiteName(),
                request.getSiteUsername(),
                request.getPassword()
        );
    }

    // 🔐 List passwords (masked only)
    @GetMapping("/list")
    public List<VaultEntryResponse> listPasswords() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return vaultService.getPasswords(username);
    }

    // 🔐 Decrypt specific entry (requires account password)
    @PostMapping("/decrypt/{id}")
    public String decryptPassword(@PathVariable Long id,
                                  @RequestParam String accountPassword) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return vaultService.decryptPassword(
                username,
                accountPassword,
                id
        );
    }

    @PutMapping("/update/{id}")
    public String updatePassword(@PathVariable Long id,
                                 @RequestParam String siteName,
                                 @RequestParam String siteUsername,
                                 @RequestParam String newPassword,
                                 @RequestParam String accountPassword) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return vaultService.updatePassword(
                username,
                accountPassword,
                id,
                siteName,
                siteUsername,
                newPassword
        );
    }

    @DeleteMapping("/delete/{id}")
    public String deletePassword(@PathVariable Long id) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return vaultService.deletePassword(username, id);
    }
}
