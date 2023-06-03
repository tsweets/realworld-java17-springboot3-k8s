package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//Content-Type: application/json; charset=utf-8
@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "users api", description = "Unauthenticated User API")
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
    public ResponseEntity<UserDTO> authenticateUser(@RequestBody UserLoginDTO dto) {
        log.info("REST (post): /api/users/login ");
        String passwordMasked = StringUtils.repeat("*", dto.getUser().getPassword().length());
        log.info("Username: {} - Password: {}", dto.getUser().getEmail(), passwordMasked);

        User user = userService.findUserByEmail(dto.getUser().getEmail());
        UserDTO userDTO = new UserDTO();

        if (user == null) {
            log.error("No User found: {}", dto);
            // return a 401 error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserDTO());
        } else {

            String token = tokenService.generateToken(user);

            userDTO = user.toDto();
            userDTO.getUser().setToken(token);
        }

        return new ResponseEntity<UserDTO>(userDTO,HttpStatus.OK);
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User Registration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User Created")
    })
    public UserDTO userRegistration(@Valid @RequestBody UserRegistrationDTO dto) {
        log.info("REST (get): /api/users");
        log.info("Registration: {}", dto);
        User user = userService.createUser(dto);
        UserDTO.User embeddedUser = UserDTO.User.builder()
                .bio(user.getBio())
                .email(user.getEmail())
                .image(user.getImageUrl())
                .username(user.getUsername())
                .build();
        UserDTO createdUser = UserDTO.builder().user(embeddedUser).build();

        return createdUser;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });

    return errors;
    }

}
