package org.beer30.realworld.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
{
    "user":{
      "username": "Jacob",
      "email": "jake@jake.jake",
      "password": "jakejake"
    }
  }
  */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder 
public class UserRegistrationDTO {
    @NotNull
    String username;
    @NotNull
    String email;
    @NotNull
    String password;
}
