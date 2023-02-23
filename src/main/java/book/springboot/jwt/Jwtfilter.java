package book.springboot.jwt;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor

public class Jwtfilter  extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;


    private String resolveToken(HttpServletRequest request) { //리퀘스트 헤더에 저장된 토큰 정보 꺼내옴
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //getHeader()
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //필터링
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String jwt = resolveToken(request);
        //위의 resolve 토큰으로 정보를 가져와
        //밑의 validateToken으로 유효 검사 tokenProvider.validateToken
        //유효할 시  Authentication을 가져와 시큐리티 컨텍스트 저장 tokenProvider.getAuthentication
        //모든 request요청은 이 필터를 거침 토큰 정보가 없거나 유효하지않으면 정상작동 x
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //반대로 Request가 정상적으로 Controller까지 도착했으면 SecurityContext에 Member ID가 존재한다는 것이 보장이 된다.

        filterChain.doFilter(request, response);
    }
}
