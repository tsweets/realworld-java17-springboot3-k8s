package org.beer30.realworld.controller;

import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.TokenService;
import org.beer30.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;


/*
 * SecurityScheme(name = "petstore_auth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "https://petstore3.swagger.io/oauth/authorize", scopes = {
		@OAuthScope(name = "write:pets", description = "modify pets in your account"),
		@OAuthScope(name = "read:pets", description = "read your pets") })))
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    /* GET /api/user
    Get Current User
    Returns UserDTO
    */

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get Current User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found User")
    })
    public UserDTO getCurrentUser(@RequestHeader (name="Authorization") String token) {
        log.info("REST (get): /api/user/");
        log.info("Token: {}", token);
        String username = tokenService.decodeToken(token).getSubject();
        log.info("User: {}",username );

        User user = userService.findUserByEmail(username);
        UserDTO dto = user.toDto();

        return dto;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Update User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Updated")
    })
    public UserDTO updateUser(@RequestBody UserDTO dto, @RequestHeader (name="Authorization") String token) {
        log.info("REST (put): /api/user/");
        log.info("Token: {}", token);
        String username = tokenService.decodeToken(token).getSubject();
        log.info("User: {}",username );

        User user = userService.findUserByEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setImageUrl(dto.getImage());

        User userUpdated = userService.updateUser(user);

        return userUpdated.toDto();
    }

}
