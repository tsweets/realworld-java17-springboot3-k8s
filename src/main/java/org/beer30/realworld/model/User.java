package org.beer30.realworld.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beer30.realworld.domain.UserDTO;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user") // NOTE: "user" is a keyword for some DBs, so don't use
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String imageUrl;
    private String bio;
    @ManyToMany
    @JoinTable(
            name = "following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    Set<User> following = new HashSet<>();
    

    public UserDTO toDto() {
        UserDTO.User embeddedUser = UserDTO.User.builder()
                .username(this.getUsername()).email(this.getEmail()).bio(this.getBio()).image(this.imageUrl)
                .build();
        UserDTO dto = UserDTO.builder().user(embeddedUser).build();
        return dto;
    }
}
