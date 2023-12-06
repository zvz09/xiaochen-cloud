package com.zvz09.xiaochen.log.server.domain;

import com.zvz09.xiaochen.common.core.page.BasePage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lizili-YF0033
 */
@Getter
@Setter
public class  OperationLogQueryBody extends BasePage {

    /**
     * 接口所在服务名称。
     * 通常为spring.application.name配置项的值。
     */
    private String serviceName;

    /**
     * HTTP 请求地址。
     */
    private String requestUrl;

    /**
     * 操作员Id。
     */
    private Long operatorId;

    /**
     * 操作员名称。
     */
    private String operatorName;

    /**
     * 业务类型
     */
    private String businessType;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$",
            message = "日期时间格式无效。请使用 yyyy-MM-dd HH:mm:ss 格式")
    @NotBlank(message = "开始时间不能为空")
    private String begin;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$",
            message = "日期时间格式无效。请使用 yyyy-MM-dd HH:mm:ss 格式")
    @NotBlank(message = "结束时间不能为空")
    private String end;
}
