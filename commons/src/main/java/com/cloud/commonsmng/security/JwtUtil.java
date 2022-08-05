package com.cloud.commonsmng.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    //jwt 有效时间为30分钟
    private static final long EXPIRE_TIME = 10 * 100000;

    private static final long MANAGE_ACCESS_TOKEN_EXPIRE_TIME = 1000 * 500;//15分钟
    private static final long MANAGE_REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 3000;//30分钟
    private static final long WECHAT_ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3600;//36小时
    private static final long WECHAT_REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 7200;//72小时（3天）

    private static final String TOKEN_SECRIT = "WSADSsdf1sasfds34311sf";

    //创建token
    public static String CreateToken(String userid, String username) {
        String token = "";
        try {
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            Date dt = new Date(System.currentTimeMillis() + MANAGE_ACCESS_TOKEN_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRIT);
            token = JWT.create()
                    .withClaim("userid", userid)
                    .withClaim("username", username)
                    .withClaim("variate", UUID.randomUUID().toString())
                    .withClaim("date", String.valueOf(System.currentTimeMillis()))
                    .withExpiresAt(dt) //设置过期时间-过期时间要大于签发时间
                    .withIssuedAt(new Date(System.currentTimeMillis())) //设置签发时间
                    .sign(algorithm); //加密
        } catch (Exception ex) {
            logger.error("创建token 出错", ex.getMessage());
        }
        return token;
    }

    //创建小程序token
    public static String CreateWXToken(String logininfokey) {
        String token = "";
        try {
            Date dt = new Date(System.currentTimeMillis() + WECHAT_ACCESS_TOKEN_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRIT);
            token = JWT.create()
                    .withClaim("logininfokey", logininfokey)
                    .withClaim("variate", UUID.randomUUID().toString())
                    .withClaim("date", String.valueOf(System.currentTimeMillis()))
                    .withExpiresAt(dt)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } catch (Exception ex) {
            logger.error("创建token 出错", ex.getMessage());
        }
        return token;
    }

    //刷新token
    public static String RefreshToken(String userid, String username) {
        String token = "";
        try {
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            Date dt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRIT);
            Map<String, Object> header = new HashMap<>();
            header.put("type", "JWT");
            header.put("alg", "HS256");
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("userid", userid)
                    .withClaim("username", username)
                    .withClaim("variate", UUID.randomUUID().toString())
                    .withClaim("date", String.valueOf(System.currentTimeMillis()))
                    .withExpiresAt(dt)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } catch (Exception ex) {
            logger.error("创建token 出错", ex.getMessage());
        }
        return token;
    }

    //解密token
    public static Map<String, String> verifyToken(String token) {
        Algorithm algorithm = null;
        Map<String, Claim> map = new HashMap<>();
        try {
            algorithm = Algorithm.HMAC256(TOKEN_SECRIT);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            map = jwt.getClaims();
            Map<String, String> resultMap = new HashMap<>(map.size());
            map.forEach((k, v) -> resultMap.put(k, v.asString()));
            return resultMap;
        } catch (Exception ex) {
            logger.error("token 转换出错", ex.getMessage());
        }
        return null;
    }

    //是否能解密token
    public static boolean verify(String token) throws Exception {
        Algorithm algorithm = null;
        Map<String, Claim> map = new HashMap<>();
        try {
            algorithm = Algorithm.HMAC256(TOKEN_SECRIT);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception ex) {
            logger.error("token 转换出错", ex.getMessage());
            return false;
        }
    }
}
