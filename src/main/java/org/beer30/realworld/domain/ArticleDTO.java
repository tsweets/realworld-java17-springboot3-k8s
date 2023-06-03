package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tsweets
 * 5/12/23 - 12:17 PM
 */
/*
{
  "article": {
    "slug": "how-to-train-your-dragon",
    "title": "How to train your dragon",
    "description": "Ever wonder how?",
    "body": "It takes a Jacobian",
    "tagList": ["dragons", "training"],
    "createdAt": "2016-02-18T03:22:56.637Z",
    "updatedAt": "2016-02-18T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
    "author": {
      "username": "jake",
      "bio": "I work at statefarm",
      "image": "https://i.stack.imgur.com/xHWG8.jpg",
      "following": false
    }
  }
}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Articles")
public class ArticleDTO {
    private ArticleEmbeddedDTO article;


   /* @Data
    @Builder
    static public class Article {
        private String slug;
        private String title;
        private String description;
        private String body;
        private String tagList; //Todo
        private String createdAt;
        private String updatedAt;
        private Boolean favorited;
        private AuthorDTO author;
    }*/

}
