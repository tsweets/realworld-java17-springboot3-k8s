package org.beer30.realworld.controller;

import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import org.springframework.test.web.servlet.MvcResult;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired    
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    

    Gson gson = new Gson();

  
    @Test
    void testAuthenticateUser() throws Exception {
        // /api/users/login

        // User to Test
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder().email("foo@example.com").username("foouser").password("password").build();
        User userCreated = userService.createUser(userRegistrationDTO);
       
        Assert.assertNotNull(userCreated);

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
        Assert.assertNotNull(dto.getToken());
        
    }

    @Test
    void testUserRegistration() throws Exception {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
            .username("user1")
            .email("user1@example.com")
            .password("user1-password")
            .build();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/")
            .content(gson.toJson(userRegistrationDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();    

    }
}
