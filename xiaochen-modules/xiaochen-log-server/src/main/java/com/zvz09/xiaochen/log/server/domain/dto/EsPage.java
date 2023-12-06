package com.zvz09.xiaochen.log.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zvz09
 */
@Data
public class EsPage<V> {
    private Long total;
    private Integer current = 1;
    private Integer size = 10;
    private List<V> records;
}
