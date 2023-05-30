package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
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
    public Article findArticleBySlug(String slug) {
        log.info("Service Call: findArticleBySlug - {}", slug);
        Article article = articleRepository.findBySlug(slug);

        return article;
    }

    @Override
    public void deleteArticleBySlug(String slug) {
        log.info("Service Call: deleteArticleBySlug - {}", slug);
        Article article = this.findArticleBySlug(slug);
        articleRepository.delete(article);
    }

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

        // slugify title
        String slug = slugify(articleCreateDTO.getTitle());
        article.setSlug(slug);

        Article articleCreated = articleRepository.save(article);
        return articleCreated;
    }

    private static String slugify(String title) {
        String slug1 = StringUtils.normalizeSpace(title);
        String slug2 = slug1.replace(" ", "-");
        return slug2;
    }

    @Override
    public Article updateArticleBySlug(String slug, ArticleUpdateDTO dto) {
        log.info("Service Call: updateArticle - {} - {} ", slug,dto);

        // Find Article
        Article article = this.findArticleBySlug(slug);
        if (dto.getBody() != null) {
            article.setBody(dto.getBody());
        }

        if (dto.getDescription() != null) {
            article.setDescription(dto.getDescription());
        }

        if (dto.getTitle() != null) {
            String slugNew = slugify(dto.getTitle());
            article.setSlug(slugNew);
            article.setTitle(dto.getTitle());
        }

        Article articleSaved = articleRepository.save(article);
        return articleSaved;
    }
}
