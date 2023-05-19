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
@Schema(description = "Create Article Request DTO")
public class ArticleCreateDTO {
    @Schema(description = "Title of the article", example = "How to train your dragon")
    @NotNull
    String title;
    @Schema(description = "Description",example = "Ever wonder how?")
    @NotNull
    String description;
    @Schema(description = "Article body", example = "You have to believe")
    @NotNull
    String body;
    //TODO tags should be a list from a table - this field is optional
    @Schema(description = "List of tags for the article (optional)", example = "[\"reactjs\", \"angularjs\", \"dragons\"]")
    String tagList;
}
