package org.beer30.realworld.controller;

import com.google.gson.Gson;
import org.beer30.realworld.domain.ArticleDTO;
import org.beer30.realworld.domain.ProfileDTO;
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

/**
 * @author tsweets
 * 5/12/23 - 12:25 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticlesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private UserService userService;

    Gson gson = new Gson();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }
    @Test
    public void createArticleTest() throws Exception {
        // Create Test User
        User testUser = ControllerTestUtils.createTestUser(userService);

        // Token for requesting user
        String token = ControllerTestUtils.getToken(this.mockMvc, testUser.getEmail(), testUser.getPassword());

        // Create Article
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername()))  // Not sure this comes back
                .andReturn();

        ArticleDTO articleDTO = gson.fromJson(result.getResponse().getContentAsString(), ArticleDTO.class);
        Assert.assertNotNull(articleDTO);
      //  Assert.assertEquals(pBio, profileDTO.getBio());

      //  System.out.println("Profile: " + profileDTO);
    }
}
