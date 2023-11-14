package com.zvz09.xiaochen.common.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizili-YF0033
 */
@Data
@Component
@ConfigurationProperties(prefix = "api.log") // 配置文件的前缀
public class ApiLogProperties {

    private List<String> excludeUrl;
}
