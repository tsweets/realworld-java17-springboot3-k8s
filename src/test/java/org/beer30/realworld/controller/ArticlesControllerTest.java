package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
import org.beer30.realworld.domain.ProfileDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.User;
import org.beer30.realworld.service.ArticleService;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    ArticleService articleService;

    Gson gson = new Gson();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }
    @Test
    public void createArticleTest() throws Exception {
        Faker faker = new Faker();

        // Create Test User
        User testUser = ControllerTestUtils.createTestUser(userService);

        // Token for requesting user
        String token = ControllerTestUtils.getToken(this.mockMvc, testUser.getEmail(), testUser.getPassword());

        // Create Article
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle(faker.book().title());
        dto.setDescription(faker.rickAndMorty().quote());
        dto.setBody(faker.lorem().paragraph(5));

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .content(gson.toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dto.getTitle()))
                .andReturn();

        ArticleDTO articleDTO = gson.fromJson(result.getResponse().getContentAsString(), ArticleDTO.class);
        Assert.assertNotNull(articleDTO);
        System.out.println("Article Returned: " + articleDTO);

        String articleSlug = articleDTO.getSlug();
        MvcResult resultGetArticle = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/{slug}", articleSlug)
                        .contentType(MediaType.APPLICATION_JSON))
//                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dto.getTitle()))
                .andReturn();
        Assert.assertNotNull(resultGetArticle);


        ArticleDTO articleFound = gson.fromJson(resultGetArticle.getResponse().getContentAsString(), ArticleDTO.class);
        Assert.assertNotNull(articleFound);
        ArticleUpdateDTO articleUpdateDTO = new ArticleUpdateDTO();
        articleUpdateDTO.setTitle(faker.book().title() + " UPDATED");
        MvcResult resultUpdateArticle = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/articles/{slug}", articleFound.getSlug())
                        .content(gson.toJson(articleUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(articleUpdateDTO.getTitle()))
                .andReturn();

        ArticleDTO resultArticleUpdatedDTO = gson.fromJson(resultUpdateArticle.getResponse().getContentAsString(), ArticleDTO.class);

        // Test List Articles - NOTE: this has a bunch of params to test
        // 1st lets add couple more Articles.
        Article article2 = createTestArticle(articleService, testUser);
        Thread.sleep(1000l); // make sure this is a time delay so that sorting works predictably
        Article article3 = createTestArticle(articleService, testUser);
        // Get all articles: Should be at least 3
        MvcResult resultGetAllArticles = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON))
//                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
         //       .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dto.getTitle()))
                .andReturn();
        Assert.assertNotNull(resultGetAllArticles);
        String articleListString = resultGetAllArticles.getResponse().getContentAsString();
        Type listOfArticleDtos = new TypeToken<ArrayList<ArticleDTO>>(){}.getType();
        List<ArticleDTO> articleDTOs = gson.fromJson(articleListString, listOfArticleDtos);
        Assert.assertNotNull(articleDTOs);
        Assert.assertTrue(articleDTOs.size() >= 3);
        // First one in the list should be the last one created if sorting is working
        Assert.assertEquals(article3.getTitle(), articleDTOs.get(0).getTitle());

  /* TODO - Need to test
    Query Parameters:
        Filter by tag: ?tag=AngularJS
        Filter by author: ?author=jake
        Favorited by user: ?favorited=jake
        Limit number of articles (default is 20): ?limit=20
        Offset/skip number of articles (default is 0): ?offset=0
    Authentication optional, will return multiple articles, ordered by most recent first
     */






        // Put at end so I have some test data
        // Do Last (Delete)
        MvcResult resultDeleteArticle = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/articles/{slug}", resultArticleUpdatedDTO.getSlug())
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assert.assertNull(articleService.findArticleBySlug(resultArticleUpdatedDTO.getSlug()));

    }

    private Article createTestArticle(ArticleService articleService,User author) {
        Faker faker = new Faker();

        Article article = new Article();
        article.setTitle(faker.book().title());
        article.setDescription(faker.rickAndMorty().quote());
        article.setBody(faker.lorem().paragraph(5));

        return articleService.createArticle(article, author);
    }
}
