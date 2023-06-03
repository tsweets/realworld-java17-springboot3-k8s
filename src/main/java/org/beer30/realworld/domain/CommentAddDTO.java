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
@Schema(description = "Add article comment DTO")
public class CommentAddDTO {
    @Schema(description = "Comment Text", example = "This is a great article")
    private String body;
}
