package z9.second.global.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.token.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.token.refresh-expiration}")
    private Long refreshExpiration;
}
