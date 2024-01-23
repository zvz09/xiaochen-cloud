package com.zvz09.xiaochen.k8s.manage.filter;

import com.zvz09.xiaochen.k8s.manage.constant.Constant;
import com.zvz09.xiaochen.k8s.manage.domain.entity.Cluster;
import com.zvz09.xiaochen.k8s.manage.domain.vo.RouteInstance;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 */
@Component
public class ProxyRoutes {
    private final List<RouteInstance> routes = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public List<RouteInstance> getRoutes() {
        lock.lock();
        try {
            return new ArrayList<>(routes); // 返回一个新的列表，防止外部修改原列表
        } finally {
            lock.unlock();
        }
    }

    public void addRoute(Cluster cluster) {
        lock.lock();
        try {
            RouteInstance route =new RouteInstance();
            route.setPath(Constant.PROXY_PREFIX+"/"+cluster.getClusterName()+"/**");
            route.setUrl(cluster.getApiServer());
            route.setToken(cluster.getToken());
            routes.add(route);
        } finally {
            lock.unlock();
        }
    }
    public void addRoute(List<Cluster> clusters) {
        lock.lock();
        try {
            clusters.forEach(cluster -> {
                RouteInstance route =new RouteInstance();
                route.setPath(Constant.PROXY_PREFIX+"/"+cluster.getClusterName()+"/**");
                route.setUrl(cluster.getApiServer());
                route.setToken(cluster.getToken());
                routes.add(route);
            });
        } finally {
            lock.unlock();
        }
    }

    public void removeRoute(String url) {
        lock.lock();
        try {
            List<RouteInstance> newRoutes = new ArrayList<>();
            routes.forEach(r -> {
                if (url.equals(r.getUrl())) {
                    newRoutes.add(r);
                }
            });
            routes.clear(); // 清空原列表
            routes.addAll(newRoutes); // 将新的列表添加回去
        } finally {
            lock.unlock();
        }
    }
    public void clear(){
        lock.lock();
        try {
            routes.clear(); // 清空原列表
        } finally {
            lock.unlock();
        }
    }
}
