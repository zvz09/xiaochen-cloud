package com.zvz09.xiaochen.k8s.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.k8s.manage.domain.dto.ClusterDTO;
import com.zvz09.xiaochen.k8s.manage.domain.entity.Cluster;

/**
* @author Administrator
* @description 针对表【k8s_cluster】的数据库操作Service
* @createDate 2024-01-22 11:25:04
*/
public interface IClusterService extends IService<Cluster> {

    void create(ClusterDTO clusterDTO);

    void delete(Long id);
}
