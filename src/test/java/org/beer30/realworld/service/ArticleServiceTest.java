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

import static org.junit.jupiter.api.Assertions.*;

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
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitle(faker.book().title());
        dto.setDescription(faker.rickAndMorty().quote());
        dto.setBody(faker.lorem().paragraph(5));

        Article article = articleService.createArticle(dto, author);
        Assert.assertNotNull(article);
        Assert.assertEquals(author.getId(), article.getAuthorId());

        // Find Article
        Article articleFoundBySlug = articleService.findArticleBySlug(article.getSlug());
        Assert.assertNotNull(articleFoundBySlug);
        Assert.assertEquals(dto.getTitle(), articleFoundBySlug.getTitle());
    }
}