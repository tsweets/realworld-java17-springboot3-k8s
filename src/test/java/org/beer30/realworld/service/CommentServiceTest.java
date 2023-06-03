package org.beer30.realworld.service;

import com.github.javafaker.Faker;
import org.beer30.realworld.controller.ControllerTestUtils;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Comment;
import org.beer30.realworld.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    Faker faker = new Faker();

    @Test
    public void addCommentTest() {
        User owner = ControllerTestUtils.createTestUser(userService);
        Assert.assertNotNull(owner);
        Article article = ControllerTestUtils.createTestArticle(articleService, owner);
        Assert.assertNotNull(article);

        String commentText = faker.harryPotter().quote();
        Comment comment = commentService.addComment(article, owner, commentText);
        Assert.assertNotNull(comment);

        List<Comment> comments = commentService.getComments(article);
        Assert.assertNotNull(comments);

        Assert.assertEquals(comment.getId(), comments.get(0).getId());

        commentService.deleteComment(comment);

        List<Comment> commentsPostDelete = commentService.getComments(article);
        Assert.assertEquals(0, commentsPostDelete.size());
    }
}