package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.beer30.realworld.domain.CommentAddDTO;
import org.beer30.realworld.domain.CommentDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Comment;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ArticleService;
import org.beer30.realworld.service.CommentService;
import org.beer30.realworld.service.UserService;
import org.junit.Assert;
import org.junit.Test;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;
    Gson gson = new Gson();
    Faker faker = new Faker();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void addCommentAPITest() throws Exception {
        String commentString = faker.lebowski().quote();
        CommentAddDTO dto = CommentAddDTO.builder().body(commentString).build();

        // Create Test Author
        User testAuthor = ControllerTestUtils.createTestUser(userService);

        // Create Test User
        User testUser = ControllerTestUtils.createTestUser(userService);

        // Token for requesting user
        String token = ControllerTestUtils.getToken(this.mockMvc, testUser.getEmail(), testUser.getPassword());

        Article article = ControllerTestUtils.createTestArticle(articleService, testAuthor);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles/{slug}/comments", article.getSlug())
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(dto.getBody()))
                .andReturn();


        Assert.assertNotNull(result);

        Comment comment2 = commentService.addComment(article, testAuthor, faker.lorem().sentence());

        MvcResult resultListComments = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/{slug}/comments", article.getSlug())

                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(dto.getBody()))
                .andReturn();

        String resultListCommentsString = resultListComments.getResponse().getContentAsString();
        Assert.assertNotNull(resultListCommentsString);

        Type listOfCommentDtos = new TypeToken<ArrayList<CommentDTO>>() {
        }.getType();
        List<CommentDTO> commentDTOS = gson.fromJson(resultListCommentsString, listOfCommentDtos);
        Assert.assertNotNull(commentDTOS);
        Assert.assertEquals(2, commentDTOS.size());

        // Delete a comment
        MvcResult resultDeleteComment = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/articles/{slug}/comments/{id}", article.getSlug(), comment2.getArticleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //      .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andReturn();


        List<Comment> commentListAfterDelete = commentService.getComments(article);
        Assert.assertNotNull(commentListAfterDelete);
        Assert.assertEquals(1, commentListAfterDelete.size());
    }

}