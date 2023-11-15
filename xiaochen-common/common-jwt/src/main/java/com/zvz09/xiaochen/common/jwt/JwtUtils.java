package com.zvz09.xiaochen.common.jwt;


import cn.hutool.core.convert.Convert;
import com.zvz09.xiaochen.common.core.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Jwt工具类
 *
 * @author zvz09
 */
public class JwtUtils {
    /**
     * 创建一个最终字符串，这个字符串称为密钥
     * <a href="https://allkeysgenerator.com/">...</a>
     * JWT最低要求的安全级别是256bit
     */
    private static final String SECRET_KEY = "3F4428472B4B6250655368566D5971337336763979244226452948404D635166";

    /**
     * 过期时间（毫秒单位）
     */
    private final static long TOKEN_EXPIRE_MILLIS = 24 * 60 * 60 * 1000;  //24h

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(currentTimeMillis))    // 设置签发时间
                .setExpiration(new Date(currentTimeMillis + TOKEN_EXPIRE_MILLIS))   // 设置过期时间
                .addClaims(claims)
                .signWith(generateKey())
                .compact();
    }

    /**
     * 生成安全密钥
     *
     * @return
     */
    public static Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(generateKey()).build().parse(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据令牌获取获取角色ID
     *
     * @param token 令牌
     * @return 角色ID
     */
    public static String getAuthorityId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_AUTHORITY_ID);
    }


    /**
     * 根据身份信息获取角色ID
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getAuthorityId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_AUTHORITY_ID);
    }

    /**
     * 根据令牌获取获取角色Code
     *
     * @param token 令牌
     * @return 角色Code
     */
    public static String getAuthorityCode(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_AUTHORITY_CODE);
    }


    /**
     * 根据身份信息获取角色Code
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getAuthorityCode(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_AUTHORITY_CODE);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }
}
