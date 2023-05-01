package org.beer30.realworld.controller;

import org.beer30.realworld.domain.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/*
 * SecurityScheme(name = "petstore_auth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(implicit = @OAuthFlow(authorizationUrl = "https://petstore3.swagger.io/oauth/authorize", scopes = {
		@OAuthScope(name = "write:pets", description = "modify pets in your account"),
		@OAuthScope(name = "read:pets", description = "read your pets") })))
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    /* GET /api/user
    Get Current User
    Returns UserDTO
    */

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get Current User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found User")
    })
    public UserDTO getCurrentUser() {
      return null;

    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Update User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Updated")
    })
    public UserDTO updateUser(@RequestBody UserDTO dto) {

      return null;
    }

}
