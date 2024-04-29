package com.zvz09.xiaochen.mc.component.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.enums.ProductEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractVpcOperation<C,R> extends AbstractBaseOperation<C,R> {

    protected final C client;

    protected final Integer maxPageSize;

    protected AbstractVpcOperation(C client, Integer maxPageSize) {
        super(client,maxPageSize);
        this.client = client;
        this.maxPageSize = maxPageSize;
    }

    public abstract VpcInstance createVpc(VpcDTO vpcDTO);

    public abstract void deleteVpc(String region, String vpcId);

    public List<VpcInstance> listVpcInstances(){
        List<VpcInstance> instances = new CopyOnWriteArrayList<>();
        this.listRegions().parallelStream().forEach(region -> {
            instances.addAll(this.listVpcInstances(region.getRegionCode()));
        });
        return instances;
    }

    public List<VpcInstance> listVpcInstances(String region){
        List<VpcInstance> instances = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(1).build();
        R response = this.executeDescribeVpcInstances(region, queryParameter);

        //处理请求
        this.addVpcInstances(region, response,instances);

        while ((queryParameter = this.vpcInstancesHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeDescribeVpcInstances(region, queryParameter);
            this.addVpcInstances(region, response,instances);
        }

        return instances;
    }

    protected abstract R executeDescribeVpcInstances(String region, QueryParameter queryParameter);

    protected abstract QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, R response);

    protected abstract void addVpcInstances(String region, R response, List<VpcInstance> instances);

    public VpcInstance describeInstance(String region, String instanceId){
        R response = this.executeDescribeVpcInstance(region, instanceId);
        return this.buildVpcInstance(region,response);
    }

    protected abstract VpcInstance buildVpcInstance(String region, R response);

    protected abstract R executeDescribeVpcInstance(String region, String instanceId);

    protected VpcInstance buildVpcInstance(String vpcId, VpcDTO vpcDTO) {
        return VpcInstance.builder()
                .provider(this.getProviderCode().getValue())
                .instanceId(vpcId)
                .instanceName(vpcDTO.getVpcName())
                .region(vpcDTO.getRegion())
                .cidrBlock(vpcDTO.getCidrBlock())
                .ipv6CidrBlock(vpcDTO.getIpv6CidrBlock())
                .build();
    }

    public Page<VSwitcheDTO> listVSwitches(String region, String vpcId, Integer pageNumber, Integer pageSize){
        Page<VSwitcheDTO> page = new Page<>(pageNumber,pageSize);
        QueryParameter queryParameter = QueryParameter.builder().pageSize(pageSize).pageNumber(pageNumber).build();
        R response = this.executeGetVSwitches(region,vpcId,queryParameter);

        //处理请求
        this.paddingVSwitchePage(vpcId,response,page);

        return page;
    }

    protected abstract R executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter);

    protected abstract void paddingVSwitchePage(String vpcId, R response, Page<VSwitcheDTO> page);

    public abstract VSwitcheDTO createVSwitch(CreateVSwitch createVSwitch);

    public abstract void deleteVSwitch(String region, String vSwitchId);

    protected VSwitcheDTO convertedVSwitche(String vSwitchId, CreateVSwitch createVSwitch) {
        return VSwitcheDTO.builder()
                .vSwitchId(vSwitchId)
                .vSwitchName(createVSwitch.getVSwitchName())
                .zoneId(createVSwitch.getZoneId())
                .cidrBlock(createVSwitch.getCidrBlock())
                .build();
    }

    public Page<SecurityGroupDTO> listSecurityGroups(String region,Integer pageNumber, Integer pageSize){
        Page<SecurityGroupDTO> page = new Page<>(pageNumber,pageSize);
        QueryParameter queryParameter = QueryParameter.builder().pageSize(pageSize).pageNumber(pageNumber).build();
        R response = this.executeGetSecurityGroups(region,queryParameter);

        //处理请求
        this.paddingSecurityGroupPage(response,page);

        return page;
    }

    protected abstract R executeGetSecurityGroups(String region, QueryParameter queryParameter);

    protected abstract void paddingSecurityGroupPage(R response, Page<SecurityGroupDTO> page);

    public ProductEnum getProductCode(){
        return ProductEnum.VPC;
    }


}
