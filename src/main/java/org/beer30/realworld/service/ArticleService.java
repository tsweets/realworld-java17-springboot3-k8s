package org.beer30.realworld.service;

import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.Favorite;
import org.beer30.realworld.model.User;

import java.util.List;

/**
 * @author tsweets
 * 5/19/23
 */
public interface ArticleService {

    Article createArticle(ArticleCreateDTO articleCreateDTO, User author);

    Article createArticle(Article article, User author);

    Article findArticleBySlug(String slug);

    void deleteArticleBySlug(String slug);

    Article updateArticleBySlug(String slug, ArticleUpdateDTO dto);

    List<Article> findArticles();

    List<Article> findFeedArticles(User user);

    Long getLikes(Article article);

    Favorite addLike(Article article, User user);

    void unLike(Article article, User user);

    Boolean isFavorted(Article article, User user);


}
