package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserLoginDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Tag;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ArticleService;
import org.beer30.realworld.service.UserService;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tsweets
 * 5/12/23 - 12:29 PM
 */
public class ControllerTestUtils {


    public static Article createTestArticle(ArticleService articleService, User author) {
        Faker faker = new Faker();

        Article article = new Article();
        article.setTitle(faker.book().title());
        article.setDescription(faker.rickAndMorty().quote());
        article.setBody(faker.lorem().paragraph(5));
        List<Tag> tagList = new ArrayList<>();
        tagList.add(Tag.builder().tag(faker.lorem().word() + "_1").build());
        tagList.add(Tag.builder().tag(faker.lorem().word() + "_2").build());
        tagList.add(Tag.builder().tag(faker.lorem().word() + "_3").build());
        article.setTagList(tagList);
        return articleService.createArticle(article, author);
    }

    public static User createTestUser(UserService userService) {
        Faker faker = new Faker();
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

        User testUser = userService.createUser(userRegistrationDTO);
        Assert.assertNotNull(testUser);

        return testUser;
    }

    public static String getToken(MockMvc mockMvc, String email, String password) throws Exception {
        Gson gson = new Gson();
        UserLoginDTO.User userEmbedded = UserLoginDTO.User.builder()
                .email(email)
                .password(password)
                .build();
        UserLoginDTO userLoginDTO = UserLoginDTO.builder().user(userEmbedded).build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
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
