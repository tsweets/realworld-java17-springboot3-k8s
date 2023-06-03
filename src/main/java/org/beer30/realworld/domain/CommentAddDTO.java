package org.beer30.realworld.domain;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

    @SerializedName("comment")
    private Comment comment;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Comment {
        @Schema(description = "Comment Text", example = "This is a great article")
        @NotNull
        @SerializedName("body")
        private String body;
    }
}
