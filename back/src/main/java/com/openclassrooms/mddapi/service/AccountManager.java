package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserPublic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.AccountDataAccess;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.util.PasswordValidator;

import java.util.Optional;
import java.util.regex.Pattern;

@Data
@Service
public class AccountManager {

    @Autowired
    private AccountDataAccess userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordHasher;

    @Autowired
    private JWTService tokenGenerator;

    private static final Pattern EMAIL_VALIDATION_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Optional<User> findAccountByEmail(final String emailAddress) {
        return userRepository.findAccountByEmail(emailAddress);
    }

    public Optional<User> getUserByEmail(final String emailAddress) {
        return userRepository.findAccountByEmail(emailAddress);
    }

    public Optional<User> getUserByUsername(final String username) {
        return userRepository.findAccountByUsername(username);
    }

    public Optional<User> getUserById(final Long accountId) {
        return userRepository.findById(accountId);
    }

    public User createUser(User newAccount) throws IllegalArgumentException {
        return registerNewAccount(newAccount);
    }

    public String updateUser(User user) {
        return modifyAccountDetails(user);
    }

    public Optional<User> findAccountByUsername(final String emailAddress) {
        return userRepository.findAccountByEmail(emailAddress);
    }

    public Optional<User> findAccountById(final Long accountId) {
        return userRepository.findById(accountId);
    }

    public User registerNewAccount(User newAccount) throws IllegalArgumentException {

        if (!validateEmailFormat(newAccount.getEmail())) {
            throw new IllegalArgumentException("Invalid email address format.");
        }

        if (!PasswordValidator.isValid(newAccount.getPassword())) {
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }

        if (userRepository.findAccountByEmail(newAccount.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email address is already in use.");
        }

        String hashedPassword = passwordHasher.encode(newAccount.getPassword());
        newAccount.setPassword(hashedPassword);
        return userRepository.save(newAccount);
    }

    public String modifyAccountDetails(User accountData) throws IllegalArgumentException {
        Optional<User> existingAccountOptional = userRepository.findById(accountData.getId());

        if (existingAccountOptional.isEmpty()) {
            throw new UsernameNotFoundException("Account not found for ID: " + accountData.getId());
        }

        User existingAccount = existingAccountOptional.get();

        if (accountData.getUsername() != null && !accountData.getUsername().isEmpty()) {
            existingAccount.setUsername(accountData.getUsername());
        }

        if (accountData.getEmail() != null && !accountData.getEmail().isEmpty()) {
            if (!validateEmailFormat(accountData.getEmail())) {
                throw new IllegalArgumentException("Invalid email address format.");
            }
            if (!accountData.getEmail().equals(existingAccount.getEmail()) &&
                    userRepository.findAccountByEmail(accountData.getEmail()).isPresent()) {
                throw new IllegalArgumentException("This email address is already in use.");
            }
            existingAccount.setEmail(accountData.getEmail());
        }

        if (accountData.getPassword() != null && !accountData.getPassword().isEmpty()) {
            if (!PasswordValidator.isValid(accountData.getPassword())) {
                throw new IllegalArgumentException("New password does not meet security requirements.");
            }
            if (!passwordHasher.matches(accountData.getPassword(), existingAccount.getPassword())) {
                String hashedPassword = passwordHasher.encode(accountData.getPassword());
                existingAccount.setPassword(hashedPassword);
            }
        }

        userRepository.save(existingAccount);

        String authToken = tokenGenerator.generateToken(existingAccount.getEmail());
        return authToken;
    }

    private boolean validateEmailFormat(String emailAddress) {
        return EMAIL_VALIDATION_PATTERN.matcher(emailAddress).matches();
    }

    public UserPublic buildPublicUserProfile(User account) {
        return new UserPublic(account);
    }

    public UserPublic getUserProfile(String username) {
        User user = userRepository.findAccountByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserPublic(user);
    }
}