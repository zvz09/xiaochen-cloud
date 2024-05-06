package com.zvz09.xiaochen.mc.component.product.ecs;

import com.zvz09.xiaochen.mc.annotation.Converter;
import com.zvz09.xiaochen.mc.component.provider.AbstractBaseOperation;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEcsOperation<C,R> extends AbstractBaseOperation<C,R> {

    protected final C client;

    protected final Integer maxPageSize;

    protected AbstractEcsOperation(C client, Integer maxPageSize) {
        super(client,maxPageSize);
        this.client = client;
        this.maxPageSize = maxPageSize;
    }

    /**
     * 查询云服务商上所有地域的所有ecs实例
     * @return
     */
    public List<EcsInstance> listEcsInstances(){
        List<EcsInstance> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listEcsInstances(region.getRegionCode()));
        });
        return instances;
    }

    /**
     * 查询云服务商上某一地域的所有ecs实例
     * @return
     */
    public List<EcsInstance> listEcsInstances(String region){
        List<EcsInstance> instances = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(Integer.valueOf(1)).build();
        R response = this.executeDescribeInstances(region, queryParameter);

        //处理请求
      this.addEcsInstances(region, response,instances);

        while ((queryParameter = this.ecsInstancesHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeDescribeInstances(region, queryParameter);
            this.addEcsInstances(region, response,instances);
        }

        return instances;
    }

    /**
     * 查询云服务商上某一地域的某一个ecs实例
     * @param region
     * @param instanceId
     * @return
     */
    public EcsInstance describeInstance(String region, String instanceId){
        R response = this.executeDescribeInstance(region, instanceId);
        return this.buildEcsInstance(region,response);
    }

    /**
     * 执行请求云服务商 ecs实例列表接口
     * @param region
     * @param queryParameter
     * @return
     */
    protected abstract R executeDescribeInstances(String region, QueryParameter queryParameter);

    /**
     * 执行请求云服务商 ecs实例详情接口
     * @param region
     * @param instanceId
     * @return
     */
    protected abstract R executeDescribeInstance(String region, String instanceId);

    /**
     * 根据云服务商返回结果判断是否还有下一页
     * @param queryParameter 本页请求体
     * @param response 云服务商返回体
     * @return 下一页请求体
     */
    protected abstract QueryParameter ecsInstancesHasNext(QueryParameter queryParameter,  R response);

    /**
     * 将云提供商返回的请求体转为DTO 集合
     * @param region
     * @param response
     * @return
     */
    protected abstract void addEcsInstances(String region, R response, List<EcsInstance> instances);

    /**
     * 将云提供商返回的请求体转为DTO
     * @param region
     * @param response
     * @return
     */
    protected abstract EcsInstance buildEcsInstance(String region, R response);

    public EcsInstance buildEcsInstance(String instanceId, String instanceName, String status, String osType, String region,
                                         String instanceSpec, String ipAddress, String instanceChargeType, String expiredTime,
                                         String detail) {
        return EcsInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(instanceId)
                .instanceName(instanceName)
                .status(status)
                .osType(osType)
                .region(region)
                .instanceSpec(instanceSpec)
                .ipAddress(ipAddress)
                .instanceChargeType(instanceChargeType)
                .expiredTime(expiredTime)
                .detail(detail)
                .build();
    }

    public List<EcsInstanceType> listAllInstanceTypes(){
        List<EcsInstanceType> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listAllInstanceTypes(region.getRegionCode()));
        });
        return instances;
    }

    public List<EcsInstanceType> listAllInstanceTypes(String region){
        List<EcsInstanceType> ecsInstanceTypes = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(Integer.valueOf(1)).build();
        R response = this.executeGetEcsInstanceTypes(region,queryParameter);

        //处理请求
        this.addEcsInstanceTypes(region,response,ecsInstanceTypes);

        while ((queryParameter = this.ecsInstanceTypesHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeGetEcsInstanceTypes(region,queryParameter);
            this.addEcsInstanceTypes(region,response,ecsInstanceTypes);
        }

        return ecsInstanceTypes;
    }

    protected abstract R executeGetEcsInstanceTypes(String region, QueryParameter queryParameter);

    protected abstract QueryParameter ecsInstanceTypesHasNext(QueryParameter queryParameter, R response) ;

    protected abstract void addEcsInstanceTypes(String region, R response, List<EcsInstanceType> ecsInstanceTypes);

    protected EcsInstanceType buildEcsInstanceType(String region, String typeId, String typeFamily, Integer cpu, String cpuModel,
                                                     Float cpuBaseFrequency, Float cpuTurboFrequency, Integer memory,
                                                     String localVolumes, String volume) {
        return EcsInstanceType.builder()
                .provider(this.getProviderCode().getValue())
                .region(region)
                .typeId(typeId)
                .typeFamily(typeFamily)
                .cpu(cpu)
                .cpuModel(cpuModel)
                .cpuBaseFrequency(cpuBaseFrequency)
                .cpuTurboFrequency(cpuTurboFrequency)
                .memory(memory)
                .localVolumes(localVolumes)
                .volume(volume)
                .build();
    }


    public List<ImageDTO> listAllImages(){
        List<ImageDTO> images = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            images.addAll(this.listAllImages(region.getRegionCode()));
        });
        return images;
    }

    public List<ImageDTO> listAllImages(String region){
        List<ImageDTO> images = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(Integer.valueOf(1)).build();
        R response = this.executeGetImages(region,queryParameter);

        //处理请求
        this.addImages(region,response,images);
        while ((queryParameter = this.imagesHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeGetImages(region,queryParameter);
            this.addImages(region,response,images);
        }

        return images;
    }

    protected abstract R executeGetImages(String region, QueryParameter queryParameter);

    protected abstract QueryParameter imagesHasNext(QueryParameter queryParameter, R response);

    protected abstract void addImages(String region, R response, List<ImageDTO> images);

    public ProductEnum getProductCode(){
        return ProductEnum.ECS;
    };
}
