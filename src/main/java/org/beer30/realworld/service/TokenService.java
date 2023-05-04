package org.beer30.realworld.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.beer30.realworld.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {
    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    JwtEncoder jwtEncoder;

    // private final JwtEnco

    // offical backend does this   jwt.sign(user, process.env.JWT_SECRET || 'superSecret', { expiresIn: '60d' });
   /*
    * Using Real App 
    {
        "user": {
            "email": "tony@example.com",
            "password": "tonypassword"
        }
    }
    */

    /*
     * Registration:
     * {
  "user": {
    "username": "tsweets",
    "email": "tony@example.com",
    "password": "tonypassword"
  }

  response:
  {
  "user": {
    "email": "tony@example.com",
    "username": "tsweets1",
    "bio": null,
    "image": "https://api.realworld.io/images/smiley-cyrus.jpeg",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRvbnlAZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6InRzd2VldHMxIiwiaWF0IjoxNjgzMDYzMzI1LCJleHAiOjE2ODgyNDczMjV9.RY5ZwUQ-IlZrwDfg-xS9TVThkuSVoARv5mzYu_EoQSg"
  }

  Decodes:
  {
  "email": "tony@example.com",
  "username": "tsweets1",
  "iat": 1683063325,
  "exp": 1688247325
}
}
}
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        // String scope = authentication.getAuthorities().stream()
        //     .map(GrantedAuthority::getAuthority)
        //     .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(60, ChronoUnit.DAYS))
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        String encodedString = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        return encodedString;
    }

    public Jwt decodeToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Token ")) {
            token = token.substring(6);
        }
        Jwt jwt = jwtDecoder.decode(token);

        return jwt;
    }
}
