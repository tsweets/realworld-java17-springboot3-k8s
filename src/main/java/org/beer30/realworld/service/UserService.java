package org.beer30.realworld.service;

import org.beer30.realworld.domain.UserDTO;
import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;

public interface UserService {

    /**
     * Create a user in the backend from a UserDTO
     * 
     * @param userRegistrationDTO user to create
     * @return newly created User
     */
    User createUser(UserRegistrationDTO userRegistrationDTO);

    /**
     * Update a user
     *
     * @param user updated user info
     * @return updated User
     */
    User updateUser(User user);

    /**
     * Lookup a User by their email 
     * 
     * @param email email to lookup by
     * @return user found
     */
    User findUserByEmail(String email);

    /**
     * Lookup a user by thier username
     * @param username to use to lookup
     * @return user found
     */
    User findUserByUsername(String username);
    
}
