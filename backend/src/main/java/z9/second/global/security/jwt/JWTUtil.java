package z9.second.global.security.jwt;

import static z9.second.global.security.constant.JWTConstant.CLAIM_KEY_USER_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.CLAIM_KEY_USER_ID;
import static z9.second.global.security.constant.JWTConstant.CLAIM_KEY_USER_ROLE;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(CLAIM_KEY_USER_ID, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(CLAIM_KEY_USER_ROLE, String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(CLAIM_KEY_USER_CATEGORY, String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }

    public String createJwt(String category, String userId, String role, long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_KEY_USER_CATEGORY, category)
                .claim(CLAIM_KEY_USER_ID, userId)
                .claim(CLAIM_KEY_USER_ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
