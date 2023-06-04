package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User's public profile")
public class ProfileDTO {
    /*
    {
  "profile": {
    "username": "jake",
    "bio": "I work at statefarm",
    "image": "https://api.realworld.io/images/smiley-cyrus.jpg",
    "following": false
  }
}
     */

    private Profile profile;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Profile {
        @Schema(description = "Username")
        private String username;
        @Schema(description = "User Biogragphy")
        private String bio;
        @Schema(description = "URL to Avatar")
        private String image;
        @Schema(description = "if the authenitcated user if following this usrname")
        private Boolean following;
    }
}
