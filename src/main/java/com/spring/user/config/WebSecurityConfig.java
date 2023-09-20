package com.spring.user.config;

import com.spring.user.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //등록할 시큐리티 서비스 멤버변수로 작성하기
    private final UserDetailService userService;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;


    //jsp가 아닌, react 이므로 정적리소스에 대해 시큐리티를 비활성화 할 필요X
//    @Bean  // 스프링 시큐리티 기능 비활성화
//    public WebSecurityCustomizer configure() {
//        return (web -> web.ignoring()
//                .requestMatchers("/static/**"));
//    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        return http
                .authorizeHttpRequests(authorization -> {authorization // 인증, 인가 설정
                        .requestMatchers(HttpMethod.DELETE, "/news/**").hasRole("ADMIN")
//                        .requestMatchers("/shopping/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/shopping").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/post/like/**").hasAnyRole("USER","ADMIN")
                        .anyRequest()
                        .permitAll();
                        })

                .formLogin(formLoginConfig -> {formLoginConfig // 폼 기반 로그인 설정
//                        .loginPage("/login") // 폼에서 날려준 정보를 토대로 로그인 처리를 해주는 주소(post)
//                        .defaultSuccessUrl("/main")
                                    .disable(); // 세션 X 토큰기반 이므로 폼로그인 막아야함
                        })

                .sessionManagement(sessionConfig ->{
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                .csrf(csrfConfig -> {csrfConfig
                        .disable();
                })

                // http요청이 들어오면, 토큰 검증 및 권한 추출 작업을 먼저 수행하게 만드는 함수. *이거 빼면 권한 설정 안됨!!!!*
                .addFilterBefore(this.tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception{
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailService) // userDetailService에 기술된 내용을 토대로 로그인 처리
                .passwordEncoder(bCryptPasswordEncoder); // 비밀번호 암호화 저장 모듈
        return builder.build();
    }
    @Bean // 패스워드 인코더로 사용할 빈 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
