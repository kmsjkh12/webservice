package book.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.h2.engine.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //시큐리티 설정들을 활성화
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .headers().frameOptions().disable().and() //h2-console을 위해 옵션들 disable
                .authorizeRequests()//url 권한설정 시작점 and matchers 사용하기 위해 선언
                .antMatchers("/", "/css/**","/images/**", //andmatchers 권한 관리 대상 지정 permitall은 전체 열람 권한
                        "/js/**","/h2-console/**").permitAll().
                antMatchers("/api/v1/**").hasRole(Role.USER.name()) //api/v1/주소를 api는 유저 권한을 가진 사람만 가능
                .anyRequest().authenticated().and().logout().logoutSuccessUrl("/")//로그아웃 기능 성공시 /
                .and().oauth2Login(). //로그인 기능에 설정 진입
                userInfoEndpoint().  // 로그인 성공 후 정보
                userService(customOAuth2UserService); //성공 후 후속 조치 userservice에 등록
    }
}
