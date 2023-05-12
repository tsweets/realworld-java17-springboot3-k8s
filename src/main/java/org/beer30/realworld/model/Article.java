package org.beer30.realworld.model;

import java.time.Instant;
import java.util.Set;

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
public class Article {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean favorited;
    private Long favoritesCount;
    private Long authorId;


}
