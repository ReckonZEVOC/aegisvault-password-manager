package com.spm.secure_password_manager.service;

import com.spm.secure_password_manager.model.User;
import com.spm.secure_password_manager.repository.UserRepository;
import com.spm.secure_password_manager.security.JwtUtil;
import com.spm.secure_password_manager.util.AesEncryptionUtil;
import com.spm.secure_password_manager.util.KeyDerivationUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AesEncryptionUtil aesUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AesEncryptionUtil aesUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.aesUtil = aesUtil;
    }

    public String register(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 1️⃣ Generate salt
        String salt = generateSalt();

        // 2️⃣ Hash password (for login)
        String passwordHash = passwordEncoder.encode(password);

        // 3️⃣ Derive key from password + salt
        SecretKeySpec derivedKey =
                KeyDerivationUtil.deriveKey(password, salt);

        // 4️⃣ Generate random master key (32 bytes)
        byte[] masterKeyBytes = new byte[32];
        new SecureRandom().nextBytes(masterKeyBytes);
        String masterKey =
                Base64.getEncoder().encodeToString(masterKeyBytes);

        // 5️⃣ Encrypt master key using derived key
        String encryptedMasterKey =
                aesUtil.encrypt(masterKey, derivedKey);  // ✅ FIXED HERE

        // 6️⃣ Save user
        User user = new User();
        user.setUsername(username);
        user.setSalt(salt);
        user.setPasswordHash(passwordHash);
        user.setEncryptedMasterKey(encryptedMasterKey);

        userRepository.save(user);

        return "User registered securely";
    }

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(username);
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
