package book.springboot.config;

import book.springboot.jwt.JwtAccessDeniedHandler;
import book.springboot.jwt.JwtAuthenticationEntryPoint;
import book.springboot.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  //비밀번호 암호화
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  //http만 사용하기위한
                .csrf().disable()       //crsf 방지 차단 , 토큰을 localstorage에 저장함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //rest api를 통해 세션 없이 토큰을 주고 받우면 데이터를 주고 받기 위한 세션 설정
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //예외 핸들링
                .accessDeniedHandler(jwtAccessDeniedHandler)          //예외 핸들링

                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll() //auth를 제외한 모든 토큰에는 requset토큰이 필요함.
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
