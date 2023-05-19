package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author tsweets
 * 5/19/23
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Article createArticle(ArticleCreateDTO articleCreateDTO, User author) {
        log.info("Service Call: createArticle - {} - by author {}", articleCreateDTO, author);

        Article article = new Article();
        // TODO this is in the wrong place
        article.setFavorited(false);
        article.setFavoritesCount(0l);
        article.setCreatedAt(Instant.now());
        article.setUpdatedAt(Instant.now());
        article.setBody(articleCreateDTO.getBody());
        article.setDescription(articleCreateDTO.getDescription());
        article.setAuthorId(author.getId());
        article.setTitle(articleCreateDTO.getTitle());

        Article articleCreated = articleRepository.save(article);
        return articleCreated;
    }
}
