package org.beer30.realworld.domain;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ArticleEmbeddedDTO {
        private String slug;
        private String title;
        private String description;
        private String body;
        private String[] tagList;
        private String createdAt;
        private String updatedAt;
        private Boolean favorited;
        private AuthorDTO author;

}
