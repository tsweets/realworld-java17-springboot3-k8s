package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.AuthorDTO;
import org.beer30.realworld.domain.CommentAddDTO;
import org.beer30.realworld.domain.CommentDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Comment;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ArticleService;
import org.beer30.realworld.service.CommentService;
import org.beer30.realworld.service.TokenService;
import org.beer30.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/articles")
@Tag(name = "comments api", description = "API for managing Comments")
public class CommentController {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @PostMapping(value = "/{slug}/comments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Add Comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment Added")
    })
    public CommentDTO addComment(@RequestBody CommentAddDTO dto, @PathVariable String slug, @RequestHeader(name = "Authorization") String token) {
        log.info("REST (post): /api/articles/{slug}/comments");
        log.info("Token: {}", token);
        log.info("Comment Data: {}", dto);
        String email = tokenService.decodeToken(token).getSubject();
        log.info("User(email): {}", email);
        log.info("Slug: {}", slug);

        User owner = userService.findUserByEmail(email);
        if (owner == null) {
            log.error("Invalid User: {}", email);
            throw new RuntimeException(); // TODO need custom exception
        }

        Article article = articleService.findArticleBySlug(slug);

        Comment comment = commentService.addComment(article, owner, dto.getBody());
        CommentDTO commentDTO = comment.toDto();

        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setBio(owner.getBio());
        authorDto.setImage(owner.getImageUrl());
        Boolean followingFlag = getFollowingFlag(owner, article);
        authorDto.setFollowing(followingFlag);
        authorDto.setUsername(owner.getUsername());
        commentDTO.setAuthor(authorDto);

        return commentDTO;
    }

    private Boolean getFollowingFlag(User owner, Article article) {
        // TODO: Refactor to service method
        Boolean followingFlag = Boolean.FALSE;
        Set<User> following = owner.getFollowing();
        if (following != null) {
            User articleAuthor = userService.findById(article.getAuthorId());
            if (following.contains(articleAuthor)) {
                followingFlag = Boolean.TRUE;
            }
        }
        // End TODO
        return followingFlag;
    }


    @GetMapping(value = "/{slug}/comments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get Article Comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Comments")
    })
    public List<CommentDTO> listComments(@PathVariable String slug, @RequestHeader(name = "Authorization") String token) {
        log.info("REST (get): /api/articles/{slug}/comments");
        log.info("Slug: {}", slug);
        log.info("Token: {}", token);

        // Optional Auth
        User user = null;
        if (token != null && !token.isEmpty()) {
            String email = tokenService.decodeToken(token).getSubject();
            log.info("User(email): {}", email);
            log.info("Slug: {}", slug);

            user = userService.findUserByEmail(email);
            if (user == null) {
                log.error("Invalid User: {}", email);
                throw new RuntimeException(); // TODO need custom exception
            }
        }

        Article article = articleService.findArticleBySlug(slug);
        List<Comment> commentList = commentService.getComments(article);
        List<CommentDTO> dtos = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO dto = comment.toDto();
            Long commentAuthorId = comment.getOwnerId();
            User commentAuthor = userService.findById(commentAuthorId);
            AuthorDTO authorDTO = AuthorDTO.builder()
                    .bio(commentAuthor.getBio())
                    .image(commentAuthor.getImageUrl())
                    .username(commentAuthor.getUsername())
                    .build();
            authorDTO.setFollowing(getFollowingFlag(commentAuthor, article));
            dto.setAuthor(authorDTO);
            dtos.add(dto);
        }
        Collections.reverse(dtos);

        return dtos;
    }


    @DeleteMapping(value = "/{slug}/comments/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Delete Comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment Deleted")
    })
    public void addDeleteComment(@PathVariable String slug, @PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        log.info("REST (post): /api/articles/{slug}/comments");
        log.info("Token: {}", token);
        log.info("Slug: {}", slug);
        log.info("Comment ID: {}", id);
        String email = tokenService.decodeToken(token).getSubject();
        log.info("User(email): {}", email);

        User owner = userService.findUserByEmail(email);
        if (owner == null) {
            log.error("Invalid User: {}", email);
            throw new RuntimeException(); // TODO need custom exception
        }

        Article article = articleService.findArticleBySlug(slug);
        Comment commentToDelete = commentService.findComment(id);
        commentService.deleteComment(commentToDelete);
    }

}
