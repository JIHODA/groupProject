package com.app.jihoproject.config;

import com.app.jihoproject.support.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AccountDetailsService accountDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests)-> authorizeHttpRequests
                                .requestMatchers("/","/sign-up","/login","/notice","/notice/**","/node_modules/**","/img/**","/css/**","/js/**")
                                .permitAll()
                        .anyRequest()
                        .authenticated()
                )
//                .csrf(AbstractHttpConfigurer::disable)
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/"))
                .logout((logout)->logout
                        .logoutSuccessUrl("/"))
                .rememberMe((rememberMe)->rememberMe
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(3600)
                        .userDetailsService(accountDetailsService));
        return http.build();
    }


}
