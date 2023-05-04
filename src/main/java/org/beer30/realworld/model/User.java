package org.beer30.realworld.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beer30.realworld.domain.UserDTO;

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
    

    public UserDTO toDto() {
        UserDTO dto = UserDTO.builder().username(this.getUsername()).email(this.getEmail()).bio(this.getBio()).image(this.imageUrl).build();
        return dto;
    }
}
