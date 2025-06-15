package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TokenDTO;
import com.openclassrooms.mddapi.model.LoginParameters;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.AuthenticationManager;
import com.openclassrooms.mddapi.service.AccountManager;
import com.openclassrooms.mddapi.service.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class LoginController {

  private AccountManager accountService;
  private AuthenticationManager loginService;
  private JWTService tokenService;

  public LoginController(AccountManager accountService, AuthenticationManager loginService, JWTService tokenService) {
    this.accountService = accountService;
    this.loginService = loginService;
    this.tokenService = tokenService;
  }

  @Operation(summary = "Inscription d'un nouvel utilisateur")
  @PostMapping("auth/register")
  public ResponseEntity<TokenDTO> registerNewAccount(@RequestBody User newUser) {
    User registeredAccount = accountService.registerNewAccount(newUser);
    String accessToken = tokenService.generateToken(registeredAccount.getEmail());
    TokenDTO response = new TokenDTO(accessToken);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Connexion d'un utilisateur")
  @PostMapping("auth/login")
  public ResponseEntity<TokenDTO> authenticateAccount(@RequestBody LoginParameters credentials) {
    User verifiedUser = loginService.validateUserCredentials(credentials.getIdentifier(), credentials.getPassword());
    String accessToken = tokenService.generateToken(verifiedUser.getEmail());
    TokenDTO response = new TokenDTO(accessToken);
    return ResponseEntity.ok(response);
  }
}