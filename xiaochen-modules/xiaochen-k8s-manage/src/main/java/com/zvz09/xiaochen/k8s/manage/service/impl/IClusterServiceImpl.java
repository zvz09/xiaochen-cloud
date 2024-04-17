package com.zvz09.xiaochen.k8s.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.util.StringUtils;
import com.zvz09.xiaochen.k8s.manage.domain.dto.ClusterDTO;
import com.zvz09.xiaochen.k8s.manage.domain.entity.Cluster;
import com.zvz09.xiaochen.k8s.manage.filter.ProxyRoutes;
import com.zvz09.xiaochen.k8s.manage.service.IClusterService;
import com.zvz09.xiaochen.k8s.manage.mapper.ClusterMapper;
import com.zvz09.xiaochen.k8s.manage.service.K8sService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【k8s_cluster】的数据库操作Service实现
* @createDate 2024-01-22 11:25:04
*/
@Service
@RequiredArgsConstructor
public class IClusterServiceImpl extends ServiceImpl<ClusterMapper, Cluster> implements IClusterService {

    private final K8sService k8sService;
    private final ProxyRoutes proxyRoutes;

    @Override
    public void create(ClusterDTO clusterDTO) {
        Cluster cluster = new Cluster();
        if(StringUtils.isEmpty(clusterDTO.getToken())){
            if(StringUtils.isEmpty(clusterDTO.getKubeconfig())){
                throw new BusinessException("token/kubeconfig不能同时为空");
            }
            clusterDTO.setToken(k8sService.createToken(clusterDTO.getKubeconfig()));
        }
        cluster.setVersion(k8sService.getK8sVersion(clusterDTO.getApiServer(),clusterDTO.getToken()));
        cluster.setClusterName(clusterDTO.getClusterName());
        cluster.setApiServer(clusterDTO.getApiServer());
        cluster.setToken(clusterDTO.getToken());

        proxyRoutes.addRoute(cluster);
        this.save(cluster);
    }

    @Override
    public void delete(Long id) {
        Cluster cluster = this.getById(id);
        if(cluster != null){
            proxyRoutes.removeRoute(cluster.getApiServer());
            this.baseMapper.deleteById(id);
        }

    }

    @Override
    public IPage<Cluster> selectPage(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()));
    }
}




