package com.oauthandsecurityandjwt.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .httpBasic(Customizer.withDefaults());
//                .formLogin((auth) -> auth.loginPage("/login")
//                        .loginProcessingUrl("/loginProc") // login 페이지에 action="/loginProc" 로 했다.
//                        .permitAll()
//                );

//        http
//                .csrf((auth) -> auth.disable());

        // maximumSession(정수) : 하나의 아이디에 대한 다중 로그인 허용 개수
        // maxSessionPreventsLogin(불린) : 다중 로그인 개수를 초과하였을 경우 처리 방법
        //   - true : 초과시 새로운 로그인 차단
        //   - false : 초과시 기존 세션 하나 삭제)
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );

        /*
            참고 : https://substantial-park-a17.notion.site/10-36136f5a91f647b499dbcb5a884aff72
            세션 고정 공격을 보호하기 위한 로그인 성공시 세션 설정 방법은 sessionManagement() 메소드의
            sessionFixation() 메소드를 통해서 설정할 수 있다.
            - sessionManagement().sessionFixation().none() : 로그인 시 세션 정보 변경 안함
            - sessionManagement().sessionFixation().newSession() : 로그인 시 세션 새로 생성
            - sessionManagement().sessionFixation().changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경
         */
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                );

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
