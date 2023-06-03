package org.beer30.realworld.service;

import com.github.javafaker.Faker;
import org.beer30.realworld.controller.ControllerTestUtils;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @Test
    public void testArticles() {
        Faker faker = new Faker();
        User author = ControllerTestUtils.createTestUser(userService);

        // Create Article
        ArticleCreateDTO.Article articleEmbedded = ArticleCreateDTO.Article.builder()
                .body(faker.lorem().paragraph(5))
                .title(faker.book().title())
                .description(faker.rickAndMorty().quote())
                .build();
        ArticleCreateDTO dto = ArticleCreateDTO.builder().article(articleEmbedded).build();

        Article article = articleService.createArticle(dto, author);
        Assert.assertNotNull(article);
        Assert.assertEquals(author.getId(), article.getAuthorId());

        // Find Article
        Article articleFoundBySlug = articleService.findArticleBySlug(article.getSlug());
        Assert.assertNotNull(articleFoundBySlug);
        Assert.assertEquals(dto.getArticle().getTitle(), articleFoundBySlug.getTitle());
    }

    @Test
    @Transactional
    public void testArticleFee() {
        //Faker faker = new Faker();

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

    }
}