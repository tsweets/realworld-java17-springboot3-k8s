package org.beer30.realworld.service;

import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;
import org.beer30.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
}
