package org.beer30.realworld.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(UserRegistrationDTO userRegistrationDTO) {
        log.info("Service Call: Create User - {}", userRegistrationDTO);
       
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());

        User userCreated = userRepository.save(user);
        log.info("User Created with ID: {}", userCreated.getId());

        return userCreated;
    }

    @Override
    public User updateUser(User user) {
        log.info("Service Call: Update User - {}", user);

        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    @Override
    public User findById(Long userId) {
        log.info("Service Call: Find by ID - {}", userId);
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("Service Call: Find by email - {}", email);

        User user = userRepository.findByEmail(email);
        log.info("Returning User: {}", user);

        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        log.info("Service Call: Find by Username - {}", username);

        User user = userRepository.findByUsername(username);
        log.info("Returning User: {}", user);

        return user;
    }

    @Override
    public User followUser(User requestingUser, User userToFollow) {
        log.info("Service Call: Follow User (UserService) - Req User: {}  Follow User: {}", requestingUser.getUsername(), userToFollow.getUsername());
        requestingUser.getFollowing().add(userToFollow);
        User updatedUser = userRepository.save(requestingUser);

        return updatedUser;
    }

    @Override
    public User unfollowUser(User requestingUser, User userToUnfollow) {
        log.info("Service Call: Un-Follow User (UserServce) - Req User: {}  Follow User: {}", requestingUser.getUsername(), userToUnfollow.getUsername());
        requestingUser.getFollowing().remove(userToUnfollow);
        User updatedUser = userRepository.save(requestingUser);

        return updatedUser;
    }

}
