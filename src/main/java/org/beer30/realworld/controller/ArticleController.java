package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
import org.beer30.realworld.domain.AuthorDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ArticleService;
import org.beer30.realworld.service.TokenService;
import org.beer30.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tsweets
 * 5/12/23 - 12:08 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/articles")
@Tag(name = "articles api", description = "API for managing Articles")
public class ArticleController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    /* Create Article
    {
        "article": {
        "title": "How to train your dragon",
                "description": "Ever wonder how?",
                "body": "You have to believe",
                "tagList": ["reactjs", "angularjs", "dragons"]
        }
    }
    */
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Create Article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Created")
    })
    public ArticleDTO createArticle(@RequestBody ArticleCreateDTO dto, @RequestHeader (name="Authorization") String token) {
        log.info("REST (post): /api/articles");
        log.info("Token: {}", token);
        log.info("Article Data: {}", dto);
        String email = tokenService.decodeToken(token).getSubject();
        log.info("User(email): {}",email );

        User author = userService.findUserByEmail(email);
        if (author == null) {
            log.error("Invalid User: {}", email);
            throw new RuntimeException(); // TODO need custom exception
        }

        Article articleCreated = articleService.createArticle(dto, author);

        ArticleDTO articleCreatedDto = articleCreated.toDto(); // Mostly filled out DTO
        AuthorDTO authorDTO = AuthorDTO.builder()
                .build();
        articleCreatedDto.setAuthor(authorDTO);

        return articleCreatedDto;
    }

    // Get Article
    @GetMapping(value = "/{slug}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get Article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Found")
    })
    public ArticleDTO getArticle(@PathVariable String slug) {
        log.info("REST (get): /api/articles");
        log.info("Slug: {}", slug);

        Article article = articleService.findArticleBySlug(slug);
        ArticleDTO articleDTO = article.toDto();

        return articleDTO;
    }
    // Update Article
    @PutMapping(value = "/{slug}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get Article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Found")
    })
    public ArticleDTO updateArticle(@PathVariable String slug, @RequestBody ArticleUpdateDTO dto, @RequestHeader (name="Authorization") String token) {
        log.info("REST (put): /api/articles");
        log.info("Slug: {}", slug);
        log.info("Token: {}", token);
        log.info("Article Data: {}", dto);
        String email = tokenService.decodeToken(token).getSubject();
        log.info("User(email): {}",email );

        User author = userService.findUserByEmail(email);
        if (author == null) {
            log.error("Invalid User: {}", email);
            throw new RuntimeException(); // TODO need custom exception
        }

        Article article = articleService.updateArticleBySlug(slug, dto);

        return article.toDto();
    }



    // List Article
    /*
    Query Parameters:
        Filter by tag: ?tag=AngularJS
        Filter by author: ?author=jake
        Favorited by user: ?favorited=jake
        Limit number of articles (default is 20): ?limit=20
        Offset/skip number of articles (default is 0): ?offset=0
    Authentication optional, will return multiple articles, ordered by most recent first
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "List Articles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles Found")
    })
    public List<ArticleDTO> listArticle() {
        log.info("REST (get): /api/articles");
      //  log.info("Slug: {}", slug);

        List<Article> articles = articleService.findArticles();

        List<ArticleDTO> dtos = new ArrayList<>();
        for (Article article : articles) {
            dtos.add(article.toDto());
        }

        Collections.reverse(dtos); // The correct order
        return dtos;
    }




    // Delete Article
    //DELETE /api/articles/:slug
    @DeleteMapping(value = "/{slug}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Delete Article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Deleted")
    })
    public void deleteArticle(@PathVariable String slug, @RequestHeader(name = "Authorization") String token) {
        log.info("REST (delete): /api/articles");
        log.info("Slug: {}", slug);

        articleService.deleteArticleBySlug(slug);
    }


    // Feed Articles
    /*
    Query Parameters:
        Filter by tag: ?tag=AngularJS
        Filter by author: ?author=jake
        Favorited by user: ?favorited=jake
        Limit number of articles (default is 20): ?limit=20
        Offset/skip number of articles (default is 0): ?offset=0
    Authentication optional, will return multiple articles, ordered by most recent first
     */
    @GetMapping(value = "/feed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Feed Articles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles by authors I follow")
    })
    public List<ArticleDTO> feedArticles(@RequestHeader(name = "Authorization") String token) {
        log.info("REST (get): /api/articles/feed");
        //  log.info("Slug: {}", slug);
        String email = tokenService.decodeToken(token).getSubject();
        log.info("User(email): {}", email);

        User user = userService.findUserByEmail(email);
        if (user == null) {
            log.error("Invalid User: {}", email);
            throw new RuntimeException(); // TODO need custom exception
        }

        List<Article> articles = articleService.findFeedArticles(user);

        List<ArticleDTO> dtos = new ArrayList<>();
        for (Article article : articles) {
            dtos.add(article.toDto());
        }

        Collections.reverse(dtos); // The correct order
        return dtos;
    }


    // Add Comments
    // Get Comments
    // Delete Comment
    // Favorite Article
    // Unfavorite Article
    // Get Tags
}
