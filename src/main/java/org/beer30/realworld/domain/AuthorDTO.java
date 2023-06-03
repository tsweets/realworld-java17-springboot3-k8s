package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tsweets
 * 5/19/23 - 11:36 AM
 */

/*
   "author": {
      "username": "jake",
      "bio": "I work at statefarm",
      "image": "https://i.stack.imgur.com/xHWG8.jpg",
      "following": false
    }
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Author")
public class AuthorDTO {
    private String username;
    private String bio;
    private String image;
    private Boolean following;

    public AuthorDTO toDto() {
        AuthorDTO dto = AuthorDTO.builder()
                .bio(this.bio)
                .following(this.following)
                .image(this.image)
                .username(this.image)
                .build();

        return dto;
    }

}
