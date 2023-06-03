package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Favorite;
import org.beer30.realworld.model.Tag;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.ArticleRepository;
import org.beer30.realworld.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tsweets
 * 5/19/23
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    FavoriteRepository favoriteRepository;


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
    public Article createArticle(Article article, User author) {
        log.info("Service Call: createArticle(Model) - {} - by author {}", article, author);

        article.setFavorited(false);
        article.setCreatedAt(Instant.now());
        article.setUpdatedAt(Instant.now());
        article.setAuthorId(author.getId());


        // slugify title
        String slug = slugify(article.getTitle());
        article.setSlug(slug);

        Article articleCreated = articleRepository.save(article);
        return articleCreated;
    }
    @Override
    public Article createArticle(ArticleCreateDTO articleCreateDTO, User author) {
        log.info("Service Call: createArticle(DTO) - {} - by author {}", articleCreateDTO, author);

        Article article = new Article();
        article.setBody(articleCreateDTO.getArticle().getBody());
        article.setDescription(articleCreateDTO.getArticle().getDescription());
        article.setTitle(articleCreateDTO.getArticle().getTitle());
        String[] tagList = articleCreateDTO.getArticle().getTagList();
        if (tagList != null) {
            List<Tag> tagListList = new ArrayList();
            for (String tag : tagList) {
                tagListList.add(Tag.builder().tag(tag).build());
            }
            //Collections.reverse(tagListList);
            article.setTagList(tagListList);
        }

        return this.createArticle(article, author);
    }

    private static String slugify(String title) {
        String slug1 = StringUtils.normalizeSpace(title);
        String slug2 = slug1.replace(" ", "-");
        return slug2;
    }

    @Override
    @Transactional
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

    @Override
    public List<Article> findArticles() {
        log.info("Service Call: findArticles");

        List<Article> articleList = articleRepository.findAll();

        return articleList;
    }

    @Override
    public List<Article> findFeedArticles(User user) {
        log.info("Service Call: findFeedArticles");

        // Get a list of Authors that the user is following
        Set<User> authorsFollowed = user.getFollowing();
        if (authorsFollowed == null) {
            log.info("Not following any authors");
            return null;
        }

        Set<Long> authorIds = new HashSet<>();
        for (User author : authorsFollowed) {
            authorIds.add(author.getId());
        }

        List<Article> articleList = articleRepository.findByAuthorIdIsIn(authorIds);
        return articleList;
    }

    @Override
    public Long getLikes(Article article) {
        log.info("Service Call: getLikes for: {}", article);

        return favoriteRepository.countFavoriteByArticleId(article.getId());
    }

    @Override
    public Favorite addLike(Article article, User user) {
        log.info("Service Call: addLIke for: {} and  user {}", article, user);
        Favorite favorite = Favorite.builder().articleId(article.getId()).userId(user.getId()).build();
        return favoriteRepository.save(favorite);
    }

    @Override
    public void unLike(Article article, User user) {
        log.info("Service Call: unLIke for: {} and  user {}", article, user);
        Favorite favorite = favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId());
        favoriteRepository.delete(favorite);
    }

    @Override
    public Boolean isFavorted(Article article, User user) {
        log.info("Service Call: isFavorited for: {} and  user {}", article, user);

        Boolean favorited = Boolean.FALSE;
        Favorite favorite = favoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId());
        if (favorite != null) {
            return Boolean.TRUE;
        }

        return favorited;
    }

}
