package io.dataease.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.dataease.auth.bo.TokenUserBO;
import io.dataease.exception.DEException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class TokenUtils {

    public static String getJwtSecret() {
        String secret = "83d923c9f1d8fcaa46cae0ed2aaa81b5";

        String DE_JWT_SECRET = System.getenv("DE_JWT_SECRET");
        if (DE_JWT_SECRET != null && !DE_JWT_SECRET.isEmpty()) {
          secret = DE_JWT_SECRET;
        }

        return secret;
    }

    public static TokenUserBO userBOByToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(TokenUtils.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            
            Long userId = jwt.getClaim("uid").asLong();
            Long oid = jwt.getClaim("oid").asLong();
            if (ObjectUtils.isEmpty(userId)) {
                DEException.throwException("token格式错误！");
            }
            return new TokenUserBO(userId, oid);
        } catch (JWTVerificationException exception) {
            // 捕获验证失败的异常
            DEException.throwException("token is invalid");
            return null; // 可以根据需要处理异常后的逻辑
        }
    }

    public static TokenUserBO validate(String token) {
        if (StringUtils.isBlank(token)) {
            String uri = ServletUtils.request().getRequestURI();
            DEException.throwException("token is empty for uri {" + uri + "}");
        }
        if (StringUtils.length(token) < 100) {
            DEException.throwException("token is invalid");
        }
        return userBOByToken(token);
    }

    public static TokenUserBO validateLinkToken(String linkToken) {
        if (StringUtils.isBlank(linkToken)) {
            String uri = ServletUtils.request().getRequestURI();
            DEException.throwException("link token is empty for uri {" + uri + "}");
        }
        if (StringUtils.length(linkToken) < 100) {
            DEException.throwException("token is invalid");
        }
        DecodedJWT jwt = JWT.decode(linkToken);
        Long userId = jwt.getClaim("uid").asLong();
        Long oid = jwt.getClaim("oid").asLong();
        if (ObjectUtils.isEmpty(userId)) {
            DEException.throwException("link token格式错误！");
        }
        return new TokenUserBO(userId, oid);
    }
}
