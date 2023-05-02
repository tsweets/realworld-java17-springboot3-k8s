package org.beer30.realworld.service.repository;

import org.beer30.realworld.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
