package book.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

//토큰값 헤더에서 추출 Dto
public class TokenDto {
    private String grantType;
    private String accessToken;

    private String refreshToken;
    private Long tokenExpiresIn;
}