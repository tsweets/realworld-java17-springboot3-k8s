package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author tsweets
 * 5/1/23 - 10:38 AM
 */

/*
{
  "user": {
    "email": "jake@jake.jake",
    "token": "jwt.token.here",
    "username": "jake",
    "bio": "I work at statefarm",
    "image": null
  }
}
 */
@Schema(description = "Users (for authentication)")
public class UserDTO {

    @Schema(description = "Email Address of the User", example = "tony@example.com")
    String email;
    @Schema(description = "JWT Token for the User")
    String token;
    @Schema(description = "Username")
    String username;
    @Schema(description = "User Biogragphy")
    String bio;
    @Schema(description = "URL to Avatar")
    String image;


    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }
    public String getToken() {
      return token;
    }
    public void setToken(String token) {
      this.token = token;
    }
    public String getUsername() {
      return username;
    }
    public void setUsername(String username) {
      this.username = username;
    }
    public String getBio() {
      return bio;
    }
    public void setBio(String bio) {
      this.bio = bio;
    }
    public String getImage() {
      return image;
    }
    public void setImage(String image) {
      this.image = image;
    }
    @Override
    public String toString() {
      return "UserDTO [email=" + email + ", token=" + token + ", username=" + username + ", bio=" + bio + ", image="
          + image + "]";
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((email == null) ? 0 : email.hashCode());
      result = prime * result + ((token == null) ? 0 : token.hashCode());
      result = prime * result + ((username == null) ? 0 : username.hashCode());
      result = prime * result + ((bio == null) ? 0 : bio.hashCode());
      result = prime * result + ((image == null) ? 0 : image.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      UserDTO other = (UserDTO) obj;
      if (email == null) {
        if (other.email != null)
          return false;
      } else if (!email.equals(other.email))
        return false;
      if (token == null) {
        if (other.token != null)
          return false;
      } else if (!token.equals(other.token))
        return false;
      if (username == null) {
        if (other.username != null)
          return false;
      } else if (!username.equals(other.username))
        return false;
      if (bio == null) {
        if (other.bio != null)
          return false;
      } else if (!bio.equals(other.bio))
        return false;
      if (image == null) {
        if (other.image != null)
          return false;
      } else if (!image.equals(other.image))
        return false;
      return true;
    }

    
    
}
