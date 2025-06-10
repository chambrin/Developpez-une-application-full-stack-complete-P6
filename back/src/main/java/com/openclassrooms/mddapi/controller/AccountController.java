package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TokenDTO;
import com.openclassrooms.mddapi.dto.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.UserPublic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.AccountManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

  private AccountManager accountService;

  public AccountController(AccountManager accountService) {
    this.accountService = accountService;
  }

  @Operation(summary = "Get user infos", description = "Allow the current authenticated user to get their own infos")
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/user/{accountId}")
  public ResponseEntity<?> retrieveAccountById(@PathVariable Long accountId) {
    Optional<User> requestedAccount = accountService.getUserById(accountId);
    if (requestedAccount.isPresent()) {
      return ResponseEntity.ok(new UserPublic(requestedAccount.get()));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur inexistant pour cet id");
    }
  }

  @Operation(summary = "Get self infos", description = "Allow the current authenticated user to get their own infos")
  @SecurityRequirement(name = "Bearer Authentication")
  @CrossOrigin(origins = "http://localhost:4200")
  @GetMapping("user/me")
  public UserPublic retrieveCurrentAccount() {
    User authenticatedUser = extractAuthenticatedUser();
    return new UserPublic(authenticatedUser);
  }

  @Operation(summary = "Update user infos", description = "Allow the current authenticated user to update their own infos")
  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping("/user/update")
  public ResponseEntity<TokenDTO> modifyAccountDetails(@RequestBody UpdateUserDTO accountModifications) {
    User authenticatedUser = extractAuthenticatedUser();
    authenticatedUser.setUsername(accountModifications.getUsername());
    authenticatedUser.setEmail(accountModifications.getEmail());
    authenticatedUser.setPassword(accountModifications.getPassword());

    String updatedToken = accountService.updateUser(authenticatedUser);
    TokenDTO response = new TokenDTO(updatedToken);
    return ResponseEntity.ok(response);
  }

  private User extractAuthenticatedUser() {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    if (currentAuth == null || !(currentAuth.getPrincipal() instanceof User)) {
      throw new RuntimeException("Authentication verification failed or invalid user principal");
    }
    return (User) currentAuth.getPrincipal();
  }
}