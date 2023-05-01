package org.beer30.realworld.controller;

import org.beer30.realworld.domain.UserLoginDTO;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired    
    private MockMvc mockMvc;
    

    Gson gson = new Gson();

    @Test
    void testAuthenticateUser() throws Exception {
        // /api/users/login
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .email("foo@example.com")
            .password("password")
            .build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .content(gson.toJson(userLoginDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
    }

    @Test
    void testUserRegistration() {
/*
 * @Test
public void givenHomePageURI_whenMockMVC_thenReturnsIndexJSPViewName() {
    this.mockMvc.perform(get("/homePage")).andDo(print())
      .andExpect(view().name("index"));
}
 */


    }
}
