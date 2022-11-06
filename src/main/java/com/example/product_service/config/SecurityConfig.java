package com.example.product_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {   
    

    @Bean
    PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/users").hasRole("ADMIN")
            .and()
            .formLogin(
                form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/users")
                    .permitAll()
            ).logout(
                logout -> logout    
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll()
            );
        return http.build();
    }
    


}
