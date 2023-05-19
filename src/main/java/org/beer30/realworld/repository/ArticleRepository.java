package org.beer30.realworld.repository;

import org.beer30.realworld.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tsweets
 * 5/19/23 - 2:11 PM
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
