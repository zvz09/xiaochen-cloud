package com.zvz09.xiaochen.log.server.domain;

import com.zvz09.xiaochen.common.core.page.BasePage;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zvz09
 */
@Getter
@Setter
public class QueryBody extends BasePage {
    private String applicationName;

    private String level;

    private String traceId;

    private String host;

    private String message;

    // 使用正则表达式定义时间格式为"yyyy-MM-dd HH:mm:ss"
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$",
            message = "日期时间格式无效。请使用 yyyy-MM-dd HH:mm:ss 格式")
    private String begin;

    // 使用正则表达式定义时间格式为"yyyy-MM-dd HH:mm:ss"
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$",
            message = "日期时间格式无效。请使用 yyyy-MM-dd HH:mm:ss 格式")
    private String end;

}
