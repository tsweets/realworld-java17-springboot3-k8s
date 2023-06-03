package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEmbeddedDTO {

    @Schema(description = "Comment ID")
    private Long id;
    @Schema(description = "Comment Creation Date")
    private String createdAt;
    @Schema(description = "Comment Update Date")
    private String updatedAt;
    @Schema(description = "Comment Text")
    private String body;
    @Schema(description = "the comment's author")
    private AuthorDTO author;
}
