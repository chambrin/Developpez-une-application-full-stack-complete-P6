package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.AccountDataAccess;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Data
@Service
public class AuthenticationManager {

    @Autowired
    private AccountDataAccess userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordHasher;

    private static final Pattern EMAIL_VALIDATION_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public User validateUserCredentials(String loginIdentifier, String providedPassword) {

        User foundUser;

        if (EMAIL_VALIDATION_PATTERN.matcher(loginIdentifier).matches()) {
            foundUser = userRepository.findAccountByEmail(loginIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user with email: " + loginIdentifier));
        } else {
            foundUser = userRepository.findAccountByUsername(loginIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user with username: " + loginIdentifier));
        }

        if (foundUser != null && passwordHasher.matches(providedPassword, foundUser.getPassword())) {
            return foundUser;
        } else {
            throw new IllegalArgumentException("Invalid credentials provided");
        }
    }
}