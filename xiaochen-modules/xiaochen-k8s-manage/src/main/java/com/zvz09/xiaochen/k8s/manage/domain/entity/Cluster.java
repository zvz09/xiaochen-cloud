package com.zvz09.xiaochen.k8s.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @TableName k8s_cluster
 */

@Getter
@Setter
@TableName(value ="k8s_cluster")
public class Cluster extends BaseEntity {

    /**
     * 
     */
    private String clusterName;

    /**
     * 
     */
    private String apiServer;

    /**
     * 
     */
    private String token;

    /**
     *
     */
    private String version;

}