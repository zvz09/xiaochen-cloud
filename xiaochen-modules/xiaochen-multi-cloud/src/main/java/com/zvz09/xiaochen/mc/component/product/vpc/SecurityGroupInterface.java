package com.zvz09.xiaochen.mc.component.product.vpc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;

import java.util.List;

public interface SecurityGroupInterface<R> {

    default Page<SecurityGroupDTO> listSecurityGroups(String region, Integer pageNumber, Integer pageSize){
        Page<SecurityGroupDTO> page = new Page<>(pageNumber,pageSize);
        QueryParameter queryParameter = QueryParameter.builder().pageSize(pageSize).pageNumber(pageNumber).build();
        R response = this.executeGetSecurityGroups(region,queryParameter);

        //处理请求
        this.paddingSecurityGroupPage(response,page);

        return page;
    }

    R executeGetSecurityGroups(String region, QueryParameter queryParameter);

    void paddingSecurityGroupPage(R response, Page<SecurityGroupDTO> page);

    String createSecurityGroup(String region ,SecurityGroupDTO securityGroupDTO);

    void deleteSecurityGroup(String region,String securityGroupId);

    default SecurityGroupDTO describeSecurityGroupAttributes(String region,String securityGroupId){
        R response = this.executeGetSecurityGroupAttributes(region,securityGroupId);
        return this.paddingSecurityGroupAttributes(response);
    }
    R executeGetSecurityGroupAttributes(String region, String securityGroupId);

    SecurityGroupDTO paddingSecurityGroupAttributes(R response);

    /**
     * 增加安全组出方向规则
     */
    void authorizeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions);

    /**
     * 增加入方向的规则
     */
    void authorizeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions);

    /**
     * 删除安全组出方向规则
     */
    void revokeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions);

    /**
     * 删除入方向的规则
     */
    void revokeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions);
}
