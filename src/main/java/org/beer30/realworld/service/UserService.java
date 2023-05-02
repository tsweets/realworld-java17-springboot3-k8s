package org.beer30.realworld.service;

import org.beer30.realworld.domain.UserRegistrationDTO;
import org.beer30.realworld.model.User;

public interface UserService {

    /**
     * Create a user in the backend from a UserDTO
     * 
     * @param userRefistrationDTO user to create
     * @return newly created User
     */
    public User createUser(UserRegistrationDTO userRegistrationDTO);

    /**
     * Lookup a User by their email 
     * 
     * @param email email to lookup by
     * @return user found
     */
    public User findUserByEmail(String email);
    
}
