package org.beer30.realworld.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beer30.realworld.domain.ArticleEmbeddedDTO;
import org.hibernate.annotations.Cascade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tsweets
 * 5/12/23 - 11:56 AM
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
@Entity
@Table(name = "article") // NOTE: "user" is a keyword for some DBs, so don't use
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;

    @ManyToMany
    @JoinTable(
            name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Tag> tagList = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean favorited;
    private Long authorId;

    public ArticleEmbeddedDTO toDto() {

        ArticleEmbeddedDTO articleEmbedded = ArticleEmbeddedDTO.builder()
                .body(this.body)
                .slug(this.slug)
                .title(this.title)
                .favorited(this.favorited)
                .description(this.description)
                .build();
        if (this.createdAt != null) {
            articleEmbedded.setCreatedAt(this.createdAt.toString());
        }
        if (this.updatedAt != null) {
            articleEmbedded.setUpdatedAt(this.updatedAt.toString());
        }

        List<String> tagString = new ArrayList<>();
        for (Tag tag : tagList) {
            tagString.add(tag.getTag());
        }
        Collections.reverse(tagString);
        String[] tagListArray = tagString.toArray(new String[0]);

        articleEmbedded.setTagList(tagListArray);
        return articleEmbedded;
    }

}
