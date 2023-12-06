package com.zvz09.xiaochen.log.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lizili-YF0033
 */
@Data
public class EsPage<V> {
    private Long total;
    private Integer current = 1;
    private Integer pageSize = 10;
    private List<V> records;
}
