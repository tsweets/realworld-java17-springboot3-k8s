package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Users (for authentication)")
public class UserDTO {

    private User user;

    @Data
    @Builder
    static public class User {
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
    }


}
