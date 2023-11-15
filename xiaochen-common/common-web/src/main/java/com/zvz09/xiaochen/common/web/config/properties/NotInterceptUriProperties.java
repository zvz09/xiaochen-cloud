package com.zvz09.xiaochen.common.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zvz09
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "not-intercept-uri")
public class NotInterceptUriProperties {

    private List<URLConfig> urlConfigs = new ArrayList<>();

    @Getter
    @Setter
    public static class URLConfig {
        private String url;
    }
}
