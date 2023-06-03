package org.beer30.realworld.service;

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
     * Lookup a user by system id (DB)
     *
     * @param userId
     * @return user found
     */
    User findById(Long userId);

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

    /**
     * Follow a User
     * @param requestingUser User that wants to follow someone
     * @param userToFollow the user that is being followed
     * @return New Requesting User
     */
    User followUser(User requestingUser, User userToFollow);

    /**
     * Un-Follow a User
     * @param requestingUser User that wants to unfollow someone
     * @param userToUnfollow the user that is being unfollowed
     * @return New Requesting User
     */
    User unfollowUser(User requestingUser, User userToUnfollow);


}
