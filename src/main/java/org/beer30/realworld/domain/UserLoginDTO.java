package org.beer30.realworld.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {

    private User user;

    @Data
    @Builder
    static public class User {
        @NotNull
        String email;
        @NotNull
        String password;
    }

}
