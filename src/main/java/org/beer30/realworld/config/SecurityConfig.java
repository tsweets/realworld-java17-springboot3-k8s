package org.beer30.realworld.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.beer30.realworld.controller.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    String sharedSecretValue = "mysecret-mysecret-mysecret-mysecret-mysecret";


    @Bean
    JwtEncoder jwtEncoder() {
        SecretKey secretKey = new SecretKeySpec(sharedSecretValue.getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(secretKey);

        return new NimbusJwtEncoder(immutableSecret);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(sharedSecretValue.getBytes(), "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        AuthTokenFilter authTokenFilter = new AuthTokenFilter();
        return authTokenFilter;
    }

    AuthenticationEntryPoint authenticationEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED); //Code 401

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()  // Create User
                        .requestMatchers("/api/users/*", "/home").permitAll()
                        .requestMatchers("/api/user", "/api/profiles/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/articles/{slug}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles").permitAll() // Might have to change this one for the filters
                        .requestMatchers(HttpMethod.POST, "/api/articles").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/articles/{slug}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/{slug}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/articles/{slug}/comments").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/articles/{slug}/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/{slug}/comments/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/articles/{slug}/favorite").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/{slug}/favorite").authenticated()
                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint));

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
