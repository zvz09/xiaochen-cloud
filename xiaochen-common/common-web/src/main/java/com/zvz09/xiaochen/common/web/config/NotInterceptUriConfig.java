package com.zvz09.xiaochen.common.web.config;

import com.zvz09.xiaochen.common.core.constant.Constants;
import com.zvz09.xiaochen.common.web.config.properties.NotInterceptUriProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zvz09.xiaochen.common.core.constant.Constants.FEIGN_PATH_PREFIX;

/**
 * @author lizili-YF0033
 * @version 1.0
 * @date 2023-06-28 11:21
 */
@Slf4j
@Configuration
public class NotInterceptUriConfig {

    @Bean
    public List<PathPattern> notInterceptUriPatternList(NotInterceptUriProperties properties) {
        List<PathPattern> pathPatternList = new ArrayList<>();
        if (properties != null && properties.getUrlConfigs() != null) {
            properties.getUrlConfigs().forEach(urlConfig -> {
                pathPatternList.add(PathPatternParser.defaultInstance.parse(urlConfig.getUrl()));
            });
        }
        Arrays.asList(Constants.EXCLUDE_PATH_PATTERNS).forEach(path -> {
            pathPatternList.add(PathPatternParser.defaultInstance.parse(path));
        });

        // /feign 开头的为内部调用不包装返回体
        pathPatternList.add(PathPatternParser.defaultInstance.parse(FEIGN_PATH_PREFIX + "/**"));

        return pathPatternList;
    }
}
