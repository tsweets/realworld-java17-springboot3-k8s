package org.beer30.realworld.controller;

import org.apache.commons.lang3.StringUtils;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.TokenService;
import org.beer30.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User Authentication", description = "Authenticate a user via oauth2")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Authenticated")
    })
    public UserDTO authenticateUser(@RequestBody UserLoginDTO dto) {
        log.info("REST (post): /api/users/login ");
        String passwordMasked = StringUtils.repeat("*", dto.getPassword().length());
        log.info("Username: {} - Password: {}", dto.getEmail(), passwordMasked);

        User user = userService.findUserByEmail(dto.getEmail());

        if (user == null) {
            // Do Something - figure out the code to return
        } 

        String token = tokenService.generateToken(user);
        

        return new UserDTO();
    }


    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User Registration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Created")
    })
    public UserDTO userRegistration(@RequestBody UserRegistrationDTO dto) {
        log.info("REST (get): /api/users/");
        log.info("Registration: {}", dto);
        User user = userService.createUser(dto);
        UserDTO createdUser = new UserDTO(user.getEmail(),  null, user.getUsername(), null, null);

        return createdUser;
    }


}
