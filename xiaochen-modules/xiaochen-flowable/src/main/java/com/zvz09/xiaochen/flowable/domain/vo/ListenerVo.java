package com.zvz09.xiaochen.flowable.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 自定义监听
 *
 * @author zvz09
 */
@Data
public class ListenerVo {

    @Schema(description = "类路径")
    private String classPath;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "支持类型")
    private List<String> typesOfSupport;
}
