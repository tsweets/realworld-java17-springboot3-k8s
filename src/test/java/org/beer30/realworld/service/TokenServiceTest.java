package org.beer30.realworld.service;

import org.beer30.realworld.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {


    @Autowired
    TokenService tokenService;


    @Test
    public void testTokens() {
        User user = User.builder()
            .email("token@exmaple.com")
            .username("tokenuser")
            .build();

        String token = tokenService.generateToken(user);
        Assert.assertNotNull(token);    

        System.out.println("Token: " + token);

        Jwt jwt = tokenService.decodeToken(token);
        Assert.assertEquals("tokenuser", jwt.getClaimAsString("username"));
        

    }

}
