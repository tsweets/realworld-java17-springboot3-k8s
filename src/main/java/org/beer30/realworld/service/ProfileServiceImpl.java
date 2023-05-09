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
    public ProfileDTO getUserProfile(String username, User requestingUser) {
        log.info("Service Call: Get User Profile - {} - by user {}", username, requestingUser);
        ProfileDTO dto = null;
        User user = userService.findUserByUsername(username);
        if (user != null) {
            dto = ProfileDTO.builder().bio(user.getBio()).image(user.getImageUrl()).username(user.getUsername()).build();
        }
        // If there is a requesting user determine if the requester is currently following
        if (requestingUser != null) {
            dto.setFollowing(requestingUser.getFollowing().contains(user));
        }

        return dto;
    }

    @Override
    public ProfileDTO followUser(String usernameToFollow, String requestingUsername) {
        log.info("Service Call: Follow User (Profile Service)");
        User requestingUser = userService.findUserByUsername(requestingUsername);
        User userToFollow = userService.findUserByUsername(usernameToFollow);
        User updatedRequestingUser = userService.followUser(requestingUser,userToFollow);

        // Lookup the profile and set the following flag appropriately
        ProfileDTO profileDTO = this.getUserProfile(usernameToFollow, updatedRequestingUser);

        return profileDTO;
    }

    @Override
    public ProfileDTO unFollowUser(String usernameToUnfollow, String requestingUsername) {
        log.info("Service Call: Un-Follow User (Profile Service)");
        User requestingUser = userService.findUserByUsername(requestingUsername);
        User userToUnfollow = userService.findUserByUsername(usernameToUnfollow);
        User updatedRequestingUser = userService.unfollowUser(requestingUser,userToUnfollow);

        // Lookup the profile and set the following flag appropriately
        ProfileDTO profileDTO = this.getUserProfile(usernameToUnfollow, updatedRequestingUser);

        return profileDTO;
    }
}
