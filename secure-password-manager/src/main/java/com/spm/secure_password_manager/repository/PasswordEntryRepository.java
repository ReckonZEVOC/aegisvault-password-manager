package com.spm.secure_password_manager.repository;

import com.spm.secure_password_manager.model.PasswordEntry;
import com.spm.secure_password_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUser(User user);
}
