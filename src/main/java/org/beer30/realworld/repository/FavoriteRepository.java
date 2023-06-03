package org.beer30.realworld.repository;

import org.beer30.realworld.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Long countFavoriteByArticleId(Long articleId);

    Favorite findByArticleIdAndUserId(Long userId, Long articleId);
}
