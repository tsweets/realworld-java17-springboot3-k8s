package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.ProfileDTO;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ProfileService;
import org.beer30.realworld.service.TokenService;
import org.beer30.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/profiles")
@Tag(name = "profile api", description = "User's Profile API")
public class ProfileController {


    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/{user}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get a user's profile - Authentication optional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found profile")
    })
    public ProfileDTO getUserProfile(@RequestHeader(name = "Authorization") String token, @PathVariable(name = "user") String username) {
        log.info("REST (get): /api/profiles/{user}");
        log.info("Username: {}", username);
        log.info("Token: {}", token);

        User requestingUser = null;
        if (token != null) {
            String requestingUsername = tokenService.decodeToken(token).getSubject();
            log.info("Requesting User: {}", requestingUsername);
            requestingUser = userService.findUserByEmail(requestingUsername);
        }

        ProfileDTO profileDTO = profileService.getUserProfile(username, requestingUser);

        return profileDTO;
    }


    @PostMapping(value = "/{username}/follow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Follow a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Follow user successful - return the profile of the user being followed")
    })
    public ProfileDTO followUser(@RequestHeader(name = "Authorization") String token, @PathVariable(name = "username") String username) {
        log.info("REST (post): /api/profiles/{username}/follow");
        log.info("Username: {}", username);
        log.info("Token: {}", token);

        User requestingUser = null;
        if (token != null) {
            String requestingUsername = tokenService.decodeToken(token).getSubject();
            log.info("Requesting User: {}", requestingUsername);
            requestingUser = userService.findUserByEmail(requestingUsername);
        }

        log.info("User: {} is requesting to follow User: {}", requestingUser.getUsername(), username);
        ProfileDTO profileDTO = profileService.followUser(username, requestingUser.getUsername());

        return profileDTO;
    }

    @DeleteMapping(value = "/{username}/follow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "UnFollow a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Un Follow user successful - return the profile of the user being no being followed anymore")
    })
    public ProfileDTO unFollowUser(@RequestHeader(name = "Authorization") String token, @PathVariable(name = "username") String username) {
        log.info("REST (post): /api/profiles/{username}/follow");
        log.info("Username: {}", username);
        log.info("Token: {}", token);

        User requestingUser = null;
        if (token != null) {
            String requestingUsername = tokenService.decodeToken(token).getSubject();
            log.info("Requesting User: {}", requestingUsername);
            requestingUser = userService.findUserByEmail(requestingUsername);
        }

        log.info("User: {} is requesting to unfollow User: {}", requestingUser.getUsername(), username);
        ProfileDTO profileDTO = profileService.unFollowUser(username, requestingUser.getUsername());

        return profileDTO;
    }

}
