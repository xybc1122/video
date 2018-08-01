package web.video.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import web.video.domain.User;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {

    public static final String SUBJECT = "videoToken";
    /**
     * 毫秒数 过期时间 当前为一周
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;

    public static final String APPSECRET = "boot999";

    /**
     * 生成jwt token
     *
     * @param user
     * @return
     */
    public static String genJsonWebToken(User user) {
        if (user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("img", user.getHeadImg())
                .setIssuedAt(new Date())//设置新的时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//过期时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }

    /**
     * 校验token 解密
     *
     * @param token
     * @return
     */
    public static Claims ccheckJWT(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }
}
