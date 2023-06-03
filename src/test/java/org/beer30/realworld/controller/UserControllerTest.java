package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    Gson gson = new Gson();

    Faker faker = new Faker();
    
    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
          .addFilter(springSecurityFilterChain).build();
    }
    // "/api/user" get current user
    @Test
    public void getCurrentUserTest() throws Exception {
        // Create Test User
        UserRegistrationDTO.User userEmbedded = UserRegistrationDTO.User.builder()
                .email("foo@example.com")
                .username("foouser")
                .password("password")
                .build();
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .user(userEmbedded)
                .build();

        User userCreated = userService.createUser(userRegistrationDTO);
        Assert.assertNotNull(userCreated);

        // Login and get token
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .email("foo@example.com")
            .password("password")
            .build();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .content(gson.toJson(userLoginDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
        
        UserDTO dto = gson.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        System.out.println(dto);
        String token = dto.getToken();
        Assert.assertNotNull(token);


        // Test without Token (should error)
        MvcResult resultNoToken = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // Add the token and should work now
        MvcResult resultWithToken = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("foouser"))
                .andReturn();

    }

    @Test
    public void updateUserTest() throws Exception {
        String testUserName = faker.name().lastName() + Instant.now().toEpochMilli();
        String email = testUserName + "@example.com";
        String password = "password" + faker.internet().password(4, 5);

        // Create Test User
        UserRegistrationDTO.User userEmbedded = UserRegistrationDTO.User.builder()
                .email(email)
                .username(testUserName)
                .password(password)
                .build();
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .user(userEmbedded)
                .build();

        User userCreated = userService.createUser(userRegistrationDTO);
        Assert.assertNotNull(userCreated);

        UserDTO userDTOUpdate = userCreated.toDto();
        Assert.assertNull(userCreated.getBio());

        String bio = faker.chuckNorris().fact().replace("Chuck Norris", testUserName);

        userDTOUpdate.setBio(bio);

        String token = this.getToken(email, password);
        // Update user
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                        .content(gson.toJson(userDTOUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUserName))
                .andReturn();

        UserDTO userUpdated = gson.fromJson(result.getResponse().getContentAsString(),UserDTO.class);
        Assert.assertNotNull(userUpdated);
        Assert.assertEquals(bio, userUpdated.getBio());

        System.out.println("BIO: " + userUpdated.getBio());



    }

    private String getToken(String email, String password) throws Exception {
        UserLoginDTO userLoginDTO = UserLoginDTO.builder().email(email).password(password).build();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .content(gson.toJson(userLoginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        UserDTO dto = gson.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        System.out.println(dto);
        String token = dto.getToken();
        Assert.assertNotNull(token);

        return token;
    }
    
}
