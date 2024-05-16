package com.zvz09.xiaochen.common.log.config;

import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

@Configuration
public class Ip2regionConfig {

    @Value("${ip2region.xdb.path}")
    private String xdbPath;

    @Bean
    public Searcher searcher() throws IOException {

        if(StringUtils.isBlank(xdbPath)){
            URL resourceUrl = Ip2regionConfig.class.getClassLoader().getResource("ip2region.xdb");
            xdbPath = resourceUrl.getPath();
        }
        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[]  cBuff = Searcher.loadContentFromFile(xdbPath);
        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        return Searcher.newWithBuffer(cBuff);
    }
}
