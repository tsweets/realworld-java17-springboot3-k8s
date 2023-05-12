package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleDTO;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @Operation(description = "Update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Updated")
    })
    public ArticleDTO createArticle(@RequestBody ArticleCreateDTO dto, @RequestHeader (name="Authorization") String token) {
        log.info("REST (post): /api/articles");
        log.info("Token: {}", token);
        String username = tokenService.decodeToken(token).getSubject();
        log.info("User: {}",username );

      /*  User user = userService.findUserByEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setImageUrl(dto.getImage());

        User userUpdated = userService.updateUser(user);
*/
        return null; //userUpdated.toDto();
    }

    // Get Article
    // Update Article
    // Delete Article
    // List Articles
    // Feed Articles
    // Add Comments
    // Get Comments
    // Delete Comment
    // Favorite Article
    // Unfavorite Article
    // Get Tags
}
