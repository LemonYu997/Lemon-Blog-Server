package lemonyu997.top.lemonapi.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//JWT工具类
public class JWTUtils {

    //JWT组成：头部、载荷、签证
    //载荷部分即要承载的数据，不应该存放敏感数据，一般存放用户id

    //自定义密钥，即签证，即私钥
    private static final String JWTTOKEN = "123456Lemon@@##$$";

    public static String createToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        String token = null;

        try {
            //头部根据JWT类型和加密算法自动生成
            JwtBuilder jwtBuilder = Jwts.builder()
                    //签证：签发算法，密钥为JwtToken
                    .signWith(SignatureAlgorithm.HS256, JWTTOKEN)
                    //载荷：body数据，要唯一，自己设置
                    .setClaims(claims)
                    //设置签发时间
                    .setIssuedAt(new Date())
                    //有效时间为1天
                    .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

            token = jwtBuilder.compact();
        } catch (ExpiredJwtException e) {
            //解决token过期的异常
            e.getClaims();
        }

        return token;
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt parse = Jwts.parser().setSigningKey(JWTTOKEN).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //测试
    public static void main(String[] args) {
        String token = JWTUtils.createToken(1);
        System.out.println(token);
        Map<String, Object> map = JWTUtils.checkToken(token);
        System.out.println(map.get("userId"));
    }
}
