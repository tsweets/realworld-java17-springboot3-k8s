package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Comments")
public class CommentMultipleDTO {
    private List<CommentEmbeddedDTO> comments = new ArrayList<>();

}
