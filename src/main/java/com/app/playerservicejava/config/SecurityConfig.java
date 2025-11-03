package com.app.playerservicejava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // replaces csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("v1/players/admin/**").hasRole("ADMIN")
                        .requestMatchers("/v1/players/guests/**").hasRole("GUEST")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // replaces httpBasic()

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin123")
                .roles("ADMIN")
                .build();

        UserDetails guest = User.withDefaultPasswordEncoder()
                .username("guest")
                .password("guest123")
                .roles("GUEST")
                .build();

        return new InMemoryUserDetailsManager(admin, guest);
    }
}
