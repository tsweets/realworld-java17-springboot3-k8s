package org.beer30.realworld.service;

import org.beer30.realworld.domain.ArticleCreateDTO;
import org.beer30.realworld.domain.ArticleUpdateDTO;
import org.beer30.realworld.model.Article;
import org.beer30.realworld.model.User;

import java.util.List;

/**
 * @author tsweets
 * 5/19/23
 */
public interface ArticleService {

    public Article createArticle(ArticleCreateDTO articleCreateDTO, User author);
    public Article createArticle(Article article, User author);

        public Article findArticleBySlug(String slug);
    public void deleteArticleBySlug(String slug);
    public Article updateArticleBySlug(String slug, ArticleUpdateDTO dto);

    List<Article> findArticles();
}
