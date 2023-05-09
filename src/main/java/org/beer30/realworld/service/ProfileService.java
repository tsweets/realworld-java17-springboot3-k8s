package org.beer30.realworld.service;

import org.beer30.realworld.domain.ProfileDTO;
import org.beer30.realworld.model.User;

public interface ProfileService {

    /**
     * Get a user profile - if a requesting user is passed in, then determine if the user is being followed
     * @param username profile to return
     * @param requestingUser user requesting profile - if null send back public profile
     * @return a user's prfile
     */
    public ProfileDTO getUserProfile(String username, User requestingUser);

    /**
     * Follow a user
     * @param usernameToFollow User to Follow
     * @param requestingUser User requesting the following
     * @return the Profile of the user being followed.
     */
    public ProfileDTO followUser(String usernameToFollow, String requestingUser);


    /**
     * Unfollow a user
     * @param usernameToUnfollow User to unfollow
     * @param requestingUser User requesting the unfollowing
     * @return the Profile of the user being not being followed.
     */
    public ProfileDTO unFollowUser(String usernameToUnfollow, String requestingUser);


}
