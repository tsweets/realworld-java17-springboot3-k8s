package org.beer30.realworld.controller;

import org.apache.commons.lang3.StringUtils;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


//Content-Type: application/json; charset=utf-8
@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "user api", description = "Unauthenticated User API")
public class UsersController {


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User Authentication", description = "Authenticate a user via oauth2")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Authenticated")
    })
    public UserDTO authenticateUser(@RequestBody UserLoginDTO dto) {
        log.info("REST: /api/users/login ");
        String passwordMasked = StringUtils.repeat("*", dto.getPassword().length());
        log.info("Username: {} - Password: {}", dto.getEmail(), passwordMasked);

        return new UserDTO();
    }


    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User Registration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Created")
    })
    public UserDTO userRegistration(@RequestBody UserDTO dto) {
      return null;
    }


}
