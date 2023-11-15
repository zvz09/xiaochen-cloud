package com.zvz09.xiaochen.flowable.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author zvz09
 */
@Data
public class StartProcessDto {
    private String processDefId;
    private Map<String, Object> variables;
}
