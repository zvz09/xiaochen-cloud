package com.zvz09.xiaochen.auth.job;

import com.zvz09.xiaochen.common.core.constant.LoginConstant;
import com.zvz09.xiaochen.common.jwt.JwtUtils;
import com.zvz09.xiaochen.job.core.annotation.XiaoChenJob;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zvz09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CleanJwtBlackListJob {
    private final RedisTemplate<String,String> redisTemplate;

    @XiaoChenJob("cleanJwtBlackListJob")
    public void doClean(){
        Long setSize = redisTemplate.opsForSet().size(LoginConstant.JWT_BLACK_LIST);
        if(setSize !=null && setSize >1000){
            log.info("JWT黑名单列表大于1000，开始清理");
            Cursor<String> cursor = redisTemplate.opsForSet().scan(LoginConstant.JWT_BLACK_LIST,
                    ScanOptions.scanOptions().match("*").count(setSize/1000).build());

            while (cursor.hasNext()) {
                String token = cursor.next();
                Claims claims = JwtUtils.parseToken(token);
                if (claims == null) {
                    log.debug("token已失效,从黑名单中剔除");
                }
            }
            //关闭cursor
            cursor.close();
        }
    }
}
