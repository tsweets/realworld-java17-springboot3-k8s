package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Comment;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Override
    public Comment addComment(Article article, User owner, String comment) {
        log.info("Service Call: Add Comment - Article {} - by user {}, Comment {}", article, owner, comment);
        Comment commententity = new Comment();
        commententity.setArticleId(article.getId());
        commententity.setOwnerId(owner.getId());
        commententity.setBody(comment);
        commententity.setCreatedAt(Instant.now());
        commententity.setUpdatedAt(Instant.now());

        Comment commentSaved = commentRepository.save(commententity);

        return commentSaved;
    }

    @Override
    public List<Comment> getComments(Article article) {
        log.info("Service Call: Get Comments - Article {}", article);
        List<Comment> commentList = commentRepository.findAllByArticleId(article.getId());

        return commentList;
    }

    @Override
    public void deleteComment(Comment comment) {
        log.info("Service Call: Delete Comment {}", comment);
        commentRepository.delete(comment);
    }

    @Override
    public Comment findComment(Long id) {
        log.info("Service Call: Find a Comment by ID {}", id);
        return commentRepository.findById(id).orElse(null);
    }
}
