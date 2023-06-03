package org.beer30.realworld.repository;

import org.beer30.realworld.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTag(String tagName);

}
