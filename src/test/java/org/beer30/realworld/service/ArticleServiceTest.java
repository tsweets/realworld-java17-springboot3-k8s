package org.beer30.realworld.service;

import com.github.javafaker.Faker;
import org.beer30.realworld.controller.ControllerTestUtils;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Favorite;
import org.beer30.realworld.model.Tag;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.ArticleRepository;
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
    ArticleRepository articleRepository;


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
    public void testArticleTags() {
        User author = ControllerTestUtils.createTestUser(userService);
        Article article = ControllerTestUtils.createTestArticle(articleService, author);

        Assert.assertNotNull(article);

        // Grab from DB
        Article articleFound = articleService.findArticleBySlug(article.getSlug());
        Assert.assertNotNull(articleFound);
        List<Tag> tags = articleFound.getTagList();
        Assert.assertNotNull(tags);
        Assert.assertEquals(3, tags.size());
        tags.stream().forEach(tag -> System.out.println(tag.getTag()));

        // Update the Article
        articleFound.setBody("UPDATED: " + article.getBody());
        Article articleUpdated = articleRepository.save(articleFound);
        Assert.assertNotNull(articleUpdated);

    }

    @Test
    @Transactional
    public void testArticleLikes() {
        User author = ControllerTestUtils.createTestUser(userService);
        Article article = ControllerTestUtils.createTestArticle(articleService, author);

        User reader1 = ControllerTestUtils.createTestUser(userService);
        User reader2 = ControllerTestUtils.createTestUser(userService);

        Assert.assertNotNull(article);

        // Grab from DB
        Article articleFound = articleService.findArticleBySlug(article.getSlug());
        Assert.assertNotNull(articleFound);
        List<Tag> tags = articleFound.getTagList();
        Assert.assertNotNull(tags);
        Assert.assertEquals(3, tags.size());
        tags.stream().forEach(tag -> System.out.println(tag.getTag()));

        Long likes = articleService.getLikes(articleFound);
        Assert.assertEquals(0, likes.longValue());

        // Lets add a like!
        Favorite fav1 = articleService.addLike(articleFound, reader1);
        Assert.assertNotNull(fav1);
        likes = articleService.getLikes(articleFound);
        Assert.assertEquals(1, likes.longValue());

        Favorite fav2 = articleService.addLike(articleFound, reader2);
        Assert.assertNotNull(fav2);
        likes = articleService.getLikes(articleFound);
        Assert.assertEquals(2, likes.longValue());

        // Unlike
        articleService.unLike(articleFound, reader1);
        likes = articleService.getLikes(articleFound);
        Assert.assertEquals(1, likes.longValue());


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