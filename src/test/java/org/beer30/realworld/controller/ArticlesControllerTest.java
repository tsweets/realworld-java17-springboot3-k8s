package org.beer30.realworld.controller;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleDTO;
import org.beer30.realworld.domain.ArticleMulitpleDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Set;

import static org.beer30.realworld.controller.ControllerTestUtils.createTestArticle;


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

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    @Transactional
    public void feedArticleTest() throws Exception {
        // Create the Authors/Reader
        User author1 = ControllerTestUtils.createTestUser(userService);
        User author2 = ControllerTestUtils.createTestUser(userService);
        User author3 = ControllerTestUtils.createTestUser(userService);
        User reader = ControllerTestUtils.createTestUser(userService);

        // Create the test Articles
        Article article1_1 = ControllerTestUtils.createTestArticle(articleService, author1);
        Article article1_2 = ControllerTestUtils.createTestArticle(articleService, author1);
        Article article2_1 = ControllerTestUtils.createTestArticle(articleService, author2);
        Article article2_2 = ControllerTestUtils.createTestArticle(articleService, author2);
        Article article3_1 = ControllerTestUtils.createTestArticle(articleService, author3);
        Article article3_2 = ControllerTestUtils.createTestArticle(articleService, author3);

        // Follow the authors
        userService.followUser(reader, author1);
        userService.followUser(reader, author2);
        userService.followUser(reader, author3);

        // Look up the reader and get the followed
        User userFound = userService.findUserByEmail(reader.getEmail());
        Assert.assertNotNull(userFound);
        Set<User> followedAuthorList = userFound.getFollowing();
        Assert.assertNotNull(followedAuthorList);
        Assert.assertEquals(3, followedAuthorList.size());

        // Get Articles
        List<Article> feedList = articleService.findFeedArticles(reader);
        Assert.assertNotNull(feedList);
        Assert.assertEquals(6, feedList.size());

        Assert.assertTrue(feedList.contains(article1_1));
        Assert.assertTrue(feedList.contains(article2_1));
        Assert.assertTrue(feedList.contains(article3_1));
        Assert.assertTrue(feedList.contains(article1_2));
        Assert.assertTrue(feedList.contains(article2_2));
        Assert.assertTrue(feedList.contains(article3_2));

        // Token for requesting user
        String token = ControllerTestUtils.getToken(this.mockMvc, userFound.getEmail(), userFound.getPassword());

        MvcResult resultGetAllArticles = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //       .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(dto.getTitle()))
                .andReturn();
        Assert.assertNotNull(resultGetAllArticles);
        String articleListString = resultGetAllArticles.getResponse().getContentAsString();
        System.out.println(articleListString);
    /*    Type listOfArticleDtos = new TypeToken<ArrayList<ArticleDTO>>() {
        }.getType();*/
        ArticleMulitpleDTO articleDTOs = gson.fromJson(articleListString, ArticleMulitpleDTO.class);
        Assert.assertNotNull(articleDTOs);
        Assert.assertEquals(6, articleDTOs.getArticles().size());

    }

    @Test
    @Transactional
    public void createArticleTest() throws Exception {
        Faker faker = new Faker();

        // Create Test User
        User testUser = ControllerTestUtils.createTestUser(userService);

        // Token for requesting user
        String token = ControllerTestUtils.getToken(this.mockMvc, testUser.getEmail(), testUser.getPassword());

        // Create Article
        String[] tagList = {"reactjs", "angularjs", "dragons"};
        ArticleCreateDTO.Article articleEmbedded = ArticleCreateDTO.Article.builder()
                .body(faker.lorem().paragraph(5))
                .title(faker.book().title())
                .description(faker.rickAndMorty().quote())
                .tagList(tagList)
                //    .tagList("['reactjs', 'angularjs', 'dragons']")
                .build();
        ArticleCreateDTO dto = ArticleCreateDTO.builder().article(articleEmbedded).build();
        String dtoString = gson.toJson(dto);
        System.out.println("DTO STRING\n" + dtoString);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .content(dtoString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.article.title").value(dto.getArticle().getTitle()))
                .andReturn();

        ArticleDTO articleDTO = gson.fromJson(result.getResponse().getContentAsString(), ArticleDTO.class);
        Assert.assertNotNull(articleDTO);
        System.out.println("Article Returned: " + articleDTO);

        String articleSlug = articleDTO.getArticle().getSlug();
        MvcResult resultGetArticle = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/{slug}", articleSlug)
                        .contentType(MediaType.APPLICATION_JSON))
//                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.article.title").value(dto.getArticle().getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.article.tagList[0]").value(dto.getArticle().getTagList()[0]))
                .andReturn();
        Assert.assertNotNull(resultGetArticle);


        ArticleDTO articleFound = gson.fromJson(resultGetArticle.getResponse().getContentAsString(), ArticleDTO.class);
        Assert.assertNotNull(articleFound);
        ArticleUpdateDTO articleUpdateDTO = new ArticleUpdateDTO();
        articleUpdateDTO.setTitle(faker.book().title() + " UPDATED");
        MvcResult resultUpdateArticle = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/articles/{slug}", articleFound.getArticle().getSlug())
                        .content(gson.toJson(articleUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.article.title").value(articleUpdateDTO.getTitle()))
                .andReturn();

        ArticleDTO resultArticleUpdatedDTO = gson.fromJson(resultUpdateArticle.getResponse().getContentAsString(), ArticleDTO.class);

        // Test List Articles - NOTE: this has a bunch of params to test
        // 1st lets add couple more Articles.
        Article article2 = createTestArticle(articleService, testUser);
        Thread.sleep(1000L); // make sure this is a time delay so that sorting works predictably
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
        //Type listOfArticleDtos = new TypeToken<ArrayList<ArticleDTO>>(){}.getType();
        ArticleMulitpleDTO articleDTOs = gson.fromJson(articleListString, ArticleMulitpleDTO.class);
        Assert.assertNotNull(articleDTOs);
        Assert.assertTrue(articleDTOs.getArticles().size() >= 3);
        // First one in the list should be the last one created if sorting is working
        Assert.assertEquals(article3.getTitle(), articleDTOs.getArticles().get(0).getTitle());

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
        MvcResult resultDeleteArticle = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/articles/{slug}", resultArticleUpdatedDTO.getArticle().getSlug())
                        .header("Authorization", "Token " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assert.assertNull(articleService.findArticleBySlug(resultArticleUpdatedDTO.getArticle().getSlug()));

    }


}
