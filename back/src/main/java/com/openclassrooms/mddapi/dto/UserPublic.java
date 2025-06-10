package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserPublic {

  public UserPublic(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.username = user.getUsername();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }
  
  public UserPublic(Long id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  private Long id;
  private String username;
  private String email;
  private Date createdAt;
  private Date updatedAt;
}
