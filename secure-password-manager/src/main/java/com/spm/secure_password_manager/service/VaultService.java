package com.spm.secure_password_manager.service;

import com.spm.secure_password_manager.dto.VaultEntryResponse;
import com.spm.secure_password_manager.model.PasswordEntry;
import com.spm.secure_password_manager.model.User;
import com.spm.secure_password_manager.repository.PasswordEntryRepository;
import com.spm.secure_password_manager.repository.UserRepository;
import com.spm.secure_password_manager.util.AesEncryptionUtil;
import com.spm.secure_password_manager.util.KeyDerivationUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Service
public class VaultService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final UserRepository userRepository;
    private final AesEncryptionUtil aesUtil;
    private final PasswordEncoder passwordEncoder;

    public VaultService(PasswordEntryRepository passwordEntryRepository,
                        UserRepository userRepository,
                        AesEncryptionUtil aesUtil,
                        PasswordEncoder passwordEncoder) {

        this.passwordEntryRepository = passwordEntryRepository;
        this.userRepository = userRepository;
        this.aesUtil = aesUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔐 Add password
    public String addPassword(String username,
                              String accountPassword,
                              String siteName,
                              String siteUsername,
                              String plainPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ VERIFY account password
        if (!passwordEncoder.matches(accountPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid account password");
        }

        // 🔑 Derive encryption key
        SecretKeySpec derivedKey =
                KeyDerivationUtil.deriveKey(accountPassword, user.getSalt());

        String encryptedPassword =
                aesUtil.encrypt(plainPassword, derivedKey);

        PasswordEntry entry = new PasswordEntry();
        entry.setSiteName(siteName);
        entry.setSiteUsername(siteUsername);
        entry.setEncryptedPassword(encryptedPassword);
        entry.setUser(user);

        passwordEntryRepository.save(entry);

        return "Password stored securely";
    }

    // 🔐 List passwords
    public List<VaultEntryResponse> getPasswords(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEntryRepository.findByUser(user)
                .stream()
                .map(entry -> new VaultEntryResponse(
                        entry.getId(),
                        entry.getSiteName(),
                        entry.getSiteUsername(),
                        "********"
                ))
                .toList();
    }

    // 🔐 Decrypt password
    public String decryptPassword(String username,
                                  String accountPassword,
                                  Long entryId) {

        PasswordEntry entry = passwordEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // ownership check
        if (!entry.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        // ✅ VERIFY account password
        if (!passwordEncoder.matches(accountPassword, entry.getUser().getPasswordHash())) {
            throw new RuntimeException("Invalid account password");
        }

        SecretKeySpec derivedKey =
                KeyDerivationUtil.deriveKey(accountPassword,
                        entry.getUser().getSalt());

        return aesUtil.decrypt(
                entry.getEncryptedPassword(),
                derivedKey
        );
    }

    // 🔐 Update password
    public String updatePassword(String username,
                                 String accountPassword,
                                 Long entryId,
                                 String siteName,
                                 String siteUsername,
                                 String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = passwordEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // ownership check
        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        // ✅ VERIFY account password
        if (!passwordEncoder.matches(accountPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid account password");
        }

        // 🔑 Derive key
        SecretKeySpec key =
                KeyDerivationUtil.deriveKey(accountPassword, user.getSalt());

        // encrypt new password
        String encryptedPassword =
                aesUtil.encrypt(newPassword, key);

        // update
        entry.setSiteName(siteName);
        entry.setSiteUsername(siteUsername);
        entry.setEncryptedPassword(encryptedPassword);

        passwordEntryRepository.save(entry);

        return "Vault entry updated successfully";
    }

    // 🔐 Delete password
    public String deletePassword(String username, Long entryId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEntry entry = passwordEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // ownership check
        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        passwordEntryRepository.delete(entry);

        return "Vault entry deleted successfully";
    }
}