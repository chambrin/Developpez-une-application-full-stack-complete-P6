package com.openclassrooms.mddapi.model;

import lombok.Data;

@Data
public class LoginParameters {

  private String identifier;
  private String password;
}
