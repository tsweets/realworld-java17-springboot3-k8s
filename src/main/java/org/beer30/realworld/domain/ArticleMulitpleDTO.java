package org.beer30.realworld.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsweets
 * 5/12/23 - 12:17 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Articles")
public class ArticleMulitpleDTO {
    private List<ArticleEmbeddedDTO> articles = new ArrayList<>();
    private Integer articlesCount = 0;

}
