package book.springboot.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//유저정보 Authentication 에 담음
//->Authentication을 시큐리티 컨텍스트에 저장
// ->시큐리티 컨텍스트 홀더에 저장

public class SecurityUtil {

    //시큐리티컨텍스트에 유저 정보가 저장되는 시점
    private SecurityUtil() { }

    //Request가 들어오면 JwtFilter의 doFilter에서 저장되는데 거기에 있는 인증정보를 꺼내서, 그 안의 id를 반환한다.
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName()); //id환 Long 타입
    }
}
