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

        /* 참고 : https://velog.io/@gmtmoney2357/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-Form-Login-%EC%9D%B8%EC%A6%9D
        http.formLogin() // form 로그인 인증 기능이 작동함
                .loginPage("/loginPage") // 사용자 정의 로그인 페이지, default: /login
                .defaultSuccessUrl("/") // 로그인 성공 후 이동 페이지
                .failureUrl("/login") // 로그인 실패 후 이동 페이지
                .usernameParameter("userId") // 아이디 파라미터명 설정, default: username
                .passwordParameter("passwd") // 패스워드 파라미터명 설정, default: password
                .loginProcessingUrl("/login_proc") // 로그인 Form Action Url, default: /login
                .successHandler( ...생략... ) // 로그인 성공 후 핸들러
                .failureHandler( ...생략... ) // 로그인 실패 후 핸들러
                .permitAll(); // loginPage 접근은 인증 없이 접근 가능
         */


        http
                .authorizeRequests((auth) -> auth
                        // .requestMatchers : 특정한 경로에 요청을 진행하고 싶다.
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
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
