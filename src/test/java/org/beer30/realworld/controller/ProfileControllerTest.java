package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.beer30.realworld.domain.ProfileDTO;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.UserRepository;
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
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    Gson gson = new Gson();

    Faker faker = new Faker();
    
    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
          .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void getUserProfileTest() throws Exception {
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

        User requestingUser = userService.createUser(userRegistrationDTO);
        Assert.assertNotNull(requestingUser);

        // Create user for the profile to return
        String pUserName = faker.name().lastName() + Instant.now().toEpochMilli();
        String pEmail = pUserName + "@example.com";
        String pPassword = "password" + faker.internet().password(4,5);
        String pBio = faker.chuckNorris().fact().replace("Chuck Norris", pUserName);
        User user = User.builder()
                .email(pEmail)
                .username(pUserName)
                .password(pPassword)
                .bio(pBio)
                .build();
        User pUser = userRepository.save(user);
        Assert.assertNotNull(pUser);

        // Token for requesting user
        String token = this.getToken(email, password);
        // Update user
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/profiles/{user}", pUserName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profile.username").value(pUserName))
                .andReturn();

        ProfileDTO profileDTO = gson.fromJson(result.getResponse().getContentAsString(),ProfileDTO.class);
        Assert.assertNotNull(profileDTO);
        Assert.assertEquals(pBio, profileDTO.getProfile().getBio());

        System.out.println("Profile: " + profileDTO);



    }

    @Test
    public void followUserTest() throws Exception {
        String requestingUserName = faker.name().lastName() + Instant.now().toEpochMilli();
        String requestingEmail = requestingUserName + "@example.com";
        String requestingPassword = "password" + faker.internet().password(4, 5);


        // Create Test User (Requesting)
        UserRegistrationDTO.User userEmbedded = UserRegistrationDTO.User.builder()
                .email(requestingEmail)
                .username(requestingUserName)
                .password(requestingPassword)
                .build();
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .user(userEmbedded)
                .build();

        User requestingUser = userService.createUser(userRegistrationDTO);
        Assert.assertNotNull(requestingUser);

        // Create user for the profile to return
        String pUserName = faker.name().lastName() + Instant.now().toEpochMilli();
        String pEmail = pUserName + "@example.com";
        String pPassword = "password" + faker.internet().password(4,5);
        String pBio = faker.chuckNorris().fact().replace("Chuck Norris", pUserName);
        User user = User.builder()
                .email(pEmail)
                .username(pUserName)
                .password(pPassword)
                .bio(pBio)
                .build();
        User pUser = userRepository.save(user);
        Assert.assertNotNull(pUser);

        // Token for requesting user
        String token = this.getToken(requestingEmail, requestingPassword);

        // Follow user
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/profiles/{puser}/follow", pUserName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ProfileDTO profileDTO = gson.fromJson(result.getResponse().getContentAsString(), ProfileDTO.class);
        Assert.assertNotNull(profileDTO);
        Assert.assertEquals(pBio, profileDTO.getProfile().getBio());

        System.out.println("Profile: " + profileDTO);
        Assert.assertEquals(true, profileDTO.getProfile().getFollowing());
        Assert.assertEquals(pUserName, profileDTO.getProfile().getUsername());

        // Now un-follow that chap
        MvcResult resultUnfollow = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/profiles/{puser}/follow", pUserName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ProfileDTO profileDTOUnfollowed = gson.fromJson(resultUnfollow.getResponse().getContentAsString(), ProfileDTO.class);
        Assert.assertNotNull(profileDTOUnfollowed);

        System.out.println("Profile: " + profileDTOUnfollowed);
        Assert.assertEquals(false, profileDTOUnfollowed.getProfile().getFollowing());
        Assert.assertEquals(pUserName, profileDTOUnfollowed.getProfile().getUsername());


    }
    private String getToken(String email, String password) throws Exception {
        UserLoginDTO.User userEmbedded = UserLoginDTO.User.builder()
                .email(email)
                .password(password)
                .build();
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .user(userEmbedded)
                .build();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .content(gson.toJson(userLoginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        UserDTO dto = gson.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        System.out.println(dto);
        String token = dto.getUser().getToken();
        Assert.assertNotNull(token);

        return token;
    }
    
}
