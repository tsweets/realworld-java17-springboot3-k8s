package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    "title": "How to train your dragon",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tagList": ["reactjs", "angularjs", "dragons"]
  }
}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Update Article Request DTO")
public class ArticleUpdateDTO {
    @Schema(description = "Title of the article", example = "How to train your dragon")
    String title;
    @Schema(description = "Description",example = "Ever wonder how?")
    String description;
    @Schema(description = "Article body", example = "You have to believe")
    String body;
}
