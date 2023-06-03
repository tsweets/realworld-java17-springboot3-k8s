package org.beer30.realworld.service;

import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Comment;
import org.beer30.realworld.model.User;

import java.util.List;

public interface CommentService {

    Comment addComment(Article article, User owner, String comment);

    List<Comment> getComments(Article article);

    void deleteComment(Comment comment);

    Comment findComment(Long id);
}
