package com.oauthandsecurityandjwt.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티가 관리
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeRequests((auth) -> auth
                        // .requestMatchers : 특정한 경로에 요청을 진행하고 싶다.
                        .requestMatchers("/", "/login").permitAll()
                        // role이 admin만 통과
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // "/my/xxx" 페이지
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // 로그인 한 사용자만 통과
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc") // login 페이지에 action="/loginProc" 로 했다.
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
