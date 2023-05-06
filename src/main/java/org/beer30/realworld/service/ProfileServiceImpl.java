package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.ProfileDTO;
import org.beer30.realworld.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    UserService userService;

    @Override
    public ProfileDTO getUserProfile(String username) {
        log.info("Service Call: Get User Profile - {}", username);
        ProfileDTO dto = null;
        User user = userService.findUserByUsername(username);
        if (user != null) {
            dto = ProfileDTO.builder().bio(user.getBio()).image(user.getImageUrl()).username(user.getUsername()).build();
        }

        return dto;
    }
}
