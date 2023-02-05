package com.wehgu.admin.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wehgu.admin.config.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static AuthProperties.Auth authProperties;

    private static ObjectMapper objectMapper;

    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        JwtUtil.authProperties = authProperties.getAuth();
    }

    @Autowired
    public static void setObjectMapper(ObjectMapper objectMapper) {
        JwtUtil.objectMapper = objectMapper;
    }

    /**
     * 由字符串生成加密key
     *
     * @return 返回一个加密的字符串数据
     * @throws Exception 抛出异常
     */
    private static SecretKey generateKey() throws Exception {
        String secret = authProperties.getSecret();
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(secret);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }



    /**
     * 创建JWT
     *
     * @param id        JWT的ID
     * @param subject   JWT主体信息，一般以Json格式的数据存储
     * @param issuer    JWT签发者
     * @param ttlMillis JWT的有效时间，单位是毫秒
     * @param claims    创建payload的私有声明，也就是自定义的JWT信息
     * @return 返回JWT
     * @throws Exception 抛出异常
     */

    public static String createToken(String id, String subject, String issuer, Long ttlMillis, Map<String, Object> claims) throws Exception {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去，一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generateKey();

        // 下面就是在为payload添加各种标准声明和私有声明了,这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder();
        // 设置签名使用的签名算法和签名使用的秘钥
        builder.signWith(signatureAlgorithm, key);
        // jti：设置JWT的ID，是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击
        builder.setId(id);
        // iat：JWT的签发时间，生成JWT的时间
        builder.setIssuedAt(new Date());
        // iss：jwt签发人
        builder.setIssuer(issuer);
        // sub：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放用户信息的，作为什么用户的唯一标志
        builder.setSubject(subject);
        if (ObjectUtils.allNotNull(claims)) {
            // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
            builder.setClaims(claims);
        }

        // exp：设置过期时间
        if (ttlMillis >= 0) {
            Long expiredMillis = System.currentTimeMillis() + ttlMillis;
            Date expiredDate = new Date(expiredMillis);
            builder.setExpiration(expiredDate);
        }
        return builder.compact();

    }

    /**
     * 创建JWT，默认配置了issuer（JWT签发者）
     *
     * @param id        JWT的ID
     * @param subject   JWT主体信息，一般以Json格式的数据存储
     * @param ttlMillis JWT的有效时间，单位是毫秒
     * @param claims    创建payload的私有声明，也就是自定义的JWT信息
     * @return 返回JWT
     * @throws Exception 抛出异常
     */
    public static String createToken(String id, String subject, Long ttlMillis, Map<String, Object> claims) throws Exception {
        return createToken(id, subject, authProperties.getIssuer(), ttlMillis, claims);
    }

    /**
     * 创建JWT，默认配置了issuer（JWT签发者），ttlMillis（有效时间）
     *
     * @param id      JWT的ID
     * @param subject JWT主体信息，一般以Json格式的数据存储
     * @param claims  创建payload的私有声明，也就是自定义的JWT信息
     * @return 返回JWT
     * @throws Exception 抛出异常
     */
    public static String createToken(String id, String subject, Map<String, Object> claims) throws Exception {
        //默认设置分钟，转化成毫秒
        long ttlMillis=authProperties.getTokenExpireTime()*60*1000;
        return createToken(id, subject, authProperties.getIssuer(), ttlMillis, claims);
    }

    /**
     * 创建JWT，默认配置了issuer（JWT签发者），ttlMillis（有效时间）,默认claims为null
     *
     * @param id      JWT的ID
     * @param subject JWT主体信息，一般以Json格式的数据存储
     * @return 返回JWT
     * @throws Exception 抛出异常
     */
    public static String createToken(String id, String subject) throws Exception {
        //默认设置分钟，转化成毫秒
        long ttlMillis=authProperties.getTokenExpireTime()*60*1000;
        return createToken(id, subject, authProperties.getIssuer(), ttlMillis, null);
    }

    /**
     * 解密JWT，如果秘钥错误，或者JWT被修改过，会直接抛出异常信息
     *
     * @param jwt JWT字符串信息
     * @return 返回JWT载荷信息
     * @throws Exception 抛出异常
     */
    public static Claims parseToken(String jwt) throws Exception {
        // 签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generateKey();
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(key)
                // 设置需要解析的jwt
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * 判断jwt是否已经过期
     *
     * @param jwt JWT字符串信息
     * @return 如果jwt已经过期，返回true，否则返回false
     * @throws Exception 抛出异常
     */
    public static boolean isExpiration(String jwt) throws Exception {
        Claims claims = JwtUtil.parseToken(jwt);
        return claims.getExpiration().before(new Date());
    }




//    public static String bodyEncrypt(String data) {
//        return AESSecretUtil.encryptToStr(data, SECRET_BODY);;
//    }
//
//    public static String bodyDecrypt(String data) {
//        return AESSecretUtil.decryptToStr(data, SECRET_BODY);
//    }
//
//    /**
//     * 在token中获取到username信息
//     */
//    public static String getUsername(String token) {
//        try {
//            DecodedJWT jwt = JWT.decode(token);
//            return jwt.getClaim("username").asString();
//        } catch (JWTDecodeException e) {
//            return null;
//        }
//    }


}
