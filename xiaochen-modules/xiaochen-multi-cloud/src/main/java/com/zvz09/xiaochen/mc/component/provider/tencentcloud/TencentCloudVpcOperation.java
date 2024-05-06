package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.CreateSecurityGroupPoliciesRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateSecurityGroupRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateSecurityGroupResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateSubnetResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.CreateVpcResponse;
import com.tencentcloudapi.vpc.v20170312.models.DeleteSecurityGroupPoliciesRequest;
import com.tencentcloudapi.vpc.v20170312.models.DeleteSecurityGroupRequest;
import com.tencentcloudapi.vpc.v20170312.models.DeleteSubnetRequest;
import com.tencentcloudapi.vpc.v20170312.models.DeleteVpcRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupPoliciesRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupPoliciesResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSecurityGroupsResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeSubnetsResponse;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsResponse;
import com.tencentcloudapi.vpc.v20170312.models.SecurityGroup;
import com.tencentcloudapi.vpc.v20170312.models.SecurityGroupPolicy;
import com.tencentcloudapi.vpc.v20170312.models.SecurityGroupPolicySet;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
import com.zvz09.xiaochen.mc.component.product.vpc.AbstractVpcOperation;
import com.zvz09.xiaochen.mc.domain.dto.CreateVSwitch;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.SecurityGroupDTO;
import com.zvz09.xiaochen.mc.domain.dto.VSwitcheDTO;
import com.zvz09.xiaochen.mc.domain.dto.VpcDTO;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import com.zvz09.xiaochen.mc.domain.entity.VpcInstance;
import com.zvz09.xiaochen.mc.service.IRegionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TencentCloudVpcOperation extends AbstractVpcOperation<TencentCloudClient, AbstractModel> implements TencentCloudBaseOperation {

    private final IRegionService regionService;
    private final TencentCloudEcsOperation ecsOperation;

    public TencentCloudVpcOperation(TencentCloudClient client, IRegionService regionService,
                                    TencentCloudEcsOperation ecsOperation) {
        super(client, Integer.valueOf(100));
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    protected AbstractModel executeGetRegions(QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(AbstractModel response, List<Region> regions) {
        regions.addAll(regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode, this.getProviderCode().getValue())));
    }

    @Override
    protected AbstractModel executeGetZones(String region, QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractModel response, List<ZoneDTO> zones) {
        zones.addAll(ecsOperation.listZones(region));
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) client.handleClient((abstractClient) -> {
            CreateVpcRequest req = super.converter.convertM2P(vpcDTO, new CreateVpcRequest());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateVpc(req);
        }, vpcDTO.getRegion(), this.getProductCode());
        return this.buildVpcInstance(response.getVpc().getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        client.handleClient((abstractClient) -> {
            DeleteVpcRequest req = new DeleteVpcRequest();
            req.setVpcId(vpcId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteVpc(req);
        }, region, this.getProductCode());
    }

    @Override
    protected AbstractModel executeDescribeVpcInstances(String region, QueryParameter queryParameter) {
        return (DescribeVpcsResponse) client.handleClient((abstractClient) -> {
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, AbstractModel response) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;

        return super.haveNext(queryParameter, resp.getTotalCount());
    }

    @Override
    protected void addVpcInstances(String region, AbstractModel response, List<VpcInstance> instances) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        Arrays.stream(resp.getVpcSet()).forEach(instance -> {
            instances.add(this.buildVpcInstance(region, instance));
        });
    }

    @Override
    protected VpcInstance buildVpcInstance(String region, AbstractModel response) {
        if (response == null) {
            return null;
        }
        Vpc vpc = (Vpc) response;
        VpcInstance vpcInstance = super.converter.convertP2M(vpc, new VpcInstance());
        vpcInstance.setProvider(this.getProviderCode().getValue());
        vpcInstance.setStatus("Available");
        vpcInstance.setRegion(region);
        vpcInstance.setDetail(JSON.toJSONString(vpc));
        return vpcInstance;
    }

    @Override
    protected AbstractModel executeDescribeVpcInstance(String region, String instanceId) {
        DescribeVpcsResponse response = (DescribeVpcsResponse) client.handleClient((abstractClient) -> {
            DescribeVpcsRequest req = new DescribeVpcsRequest();
            req.setVpcIds(new String[]{instanceId});
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeVpcs(req);
        }, region, this.getProductCode());
        if (response.getVpcSet() != null && response.getVpcSet().length > 0) {
            return response.getVpcSet()[0];
        }
        return null;
    }

    @Override
    protected AbstractModel executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter) {
        return (DescribeSubnetsResponse) client.handleClient((abstractClient) -> {
            DescribeSubnetsRequest req = new DescribeSubnetsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSubnets(req);
        }, region, this.getProductCode());
    }

    @Override
    protected void paddingVSwitchePage(String vpcId, AbstractModel response, Page<VSwitcheDTO> page) {
        DescribeSubnetsResponse resp = (DescribeSubnetsResponse) response;
        page.setTotal(resp.getTotalCount());
        page.setRecords(Arrays.stream(resp.getSubnetSet())
                .map(vSwitch -> super.converter.convertP2M(vSwitch, new VSwitcheDTO())).toList());
    }

    @Override
    public String createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) client.handleClient((abstractClient) -> {
            CreateSubnetRequest req = super.converter.convertM2P(createVSwitch, new CreateSubnetRequest());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateSubnet(req);
        }, createVSwitch.getRegionId(), this.getProductCode());
        return response.getSubnet().getSubnetId();
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        client.handleClient((abstractClient) -> {
            DeleteSubnetRequest req = new DeleteSubnetRequest();
            req.setSubnetId(vSwitchId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteSubnet(req);
        }, region, this.getProductCode());
    }

    @Override
    public AbstractModel executeGetSecurityGroups(String region, QueryParameter queryParameter) {
        return (DescribeSecurityGroupsResponse) client.handleClient((abstractClient) -> {
            DescribeSecurityGroupsRequest req = new DescribeSecurityGroupsRequest();
            req.setOffset(String.valueOf(queryParameter.getOffset()));
            req.setLimit(String.valueOf(queryParameter.getPageSize()));
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSecurityGroups(req);
        }, region, this.getProductCode());
    }

    @Override
    public void paddingSecurityGroupPage(AbstractModel response, Page<SecurityGroupDTO> page) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) response;
        page.setTotal(resp.getTotalCount());
        List<SecurityGroupDTO> records = new ArrayList<>();

        Arrays.stream(resp.getSecurityGroupSet()).forEach(securityGroup -> {
            records.add(super.converter.convertP2M(securityGroup, new SecurityGroupDTO()));
        });
        page.setRecords(records);
    }

    @Override
    public String createSecurityGroup(String region, SecurityGroupDTO securityGroupDTO) {
        CreateSecurityGroupResponse response = (CreateSecurityGroupResponse) client.handleClient((abstractClient) -> {
            CreateSecurityGroupRequest req = super.converter.convertM2P(securityGroupDTO, new CreateSecurityGroupRequest());
            VpcClient client = (VpcClient) abstractClient;
            return client.CreateSecurityGroup(req);
        }, region, this.getProductCode());
        return response.getSecurityGroup().getSecurityGroupId();
    }

    @Override
    public void deleteSecurityGroup(String region, String securityGroupId) {
        client.handleClient((abstractClient) -> {
            DeleteSecurityGroupRequest req = new DeleteSecurityGroupRequest();
            req.setSecurityGroupId(securityGroupId);
            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteSecurityGroup(req);
        }, region, this.getProductCode());
    }

    @Override
    public SecurityGroupDTO describeSecurityGroupAttributes(String region, String securityGroupId) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) client.handleClient((abstractClient) -> {
            DescribeSecurityGroupsRequest req = new DescribeSecurityGroupsRequest();
            req.setOffset(String.valueOf(0));
            req.setLimit(String.valueOf(1));
            req.setSecurityGroupIds(new String[]{securityGroupId});
            VpcClient client = (VpcClient) abstractClient;
            return client.DescribeSecurityGroups(req);
        }, region, this.getProductCode());

        SecurityGroupDTO securityGroupDTO = new SecurityGroupDTO();
        if (resp.getTotalCount() > 0) {
            SecurityGroup securityGroup = resp.getSecurityGroupSet()[0];

            DescribeSecurityGroupPoliciesResponse describeSecurityGroupPoliciesResponse =
                    (DescribeSecurityGroupPoliciesResponse) client.handleClient((abstractClient) -> {
                        DescribeSecurityGroupPoliciesRequest req = new DescribeSecurityGroupPoliciesRequest();
                        req.setSecurityGroupId(securityGroup.getSecurityGroupId());
                        VpcClient client = (VpcClient) abstractClient;
                        return client.DescribeSecurityGroupPolicies(req);
                    }, region, this.getProductCode());

            securityGroupDTO = super.converter.convertP2M(securityGroup, securityGroupDTO);
            List<SecurityGroupDTO.PermissionDTO> permissions = new ArrayList<>();
            Arrays.stream(describeSecurityGroupPoliciesResponse.getSecurityGroupPolicySet().getEgress()).forEach(egress -> {
                SecurityGroupDTO.PermissionDTO permissionDTO = super.converter.convertP2M(egress, new SecurityGroupDTO.PermissionDTO());
                permissionDTO.setDirection("egress");
                permissions.add(permissionDTO);
            });

            Arrays.stream(describeSecurityGroupPoliciesResponse.getSecurityGroupPolicySet().getIngress()).forEach(egress -> {
                SecurityGroupDTO.PermissionDTO permissionDTO = super.converter.convertP2M(egress, new SecurityGroupDTO.PermissionDTO());
                permissionDTO.setDirection("ingress");
                permissions.add(permissionDTO);
            });

            securityGroupDTO.setPermissions(permissions);
        }
        return securityGroupDTO;

    }

    @Override
    public AbstractModel executeGetSecurityGroupAttributes(String region, String securityGroupId) {
        return null;
    }

    @Override
    public SecurityGroupDTO paddingSecurityGroupAttributes(AbstractModel response) {
        return null;
    }


    @Override
    public void authorizeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        this.executeAuthorizeSecurityGroupPolicies(region, securityGroupId, permissions, "egress");
    }

    @Override
    public void authorizeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        this.executeAuthorizeSecurityGroupPolicies(region, securityGroupId, permissions, "ingress");
    }

    /**
     *
     * @param region
     * @param securityGroupId
     * @param permissions
     * @param direction  访问类型。ingress（默认）：入方向。 egress：出方向。
     */
    private void executeAuthorizeSecurityGroupPolicies(String region, String securityGroupId,
                                                       List<SecurityGroupDTO.PermissionDTO> permissions,String direction) {
        client.handleClient((abstractClient) -> {

            CreateSecurityGroupPoliciesRequest req = new CreateSecurityGroupPoliciesRequest();
            req.setSecurityGroupId(securityGroupId);
            SecurityGroupPolicySet securityGroupPolicySet = new SecurityGroupPolicySet();

            List<SecurityGroupPolicy> securityGroupPolicyList = permissions.stream()
                    .map(permission -> {
                        SecurityGroupPolicy securityGroupPolicy = super.converter.convertM2P(permission, new SecurityGroupPolicy());
                        securityGroupPolicy.setPort(permission.getPortRange().replaceAll("/","-"));
                        return securityGroupPolicy;
                    }).toList();

            if("egress".equals(direction)){
                securityGroupPolicySet.setEgress(securityGroupPolicyList.toArray(new SecurityGroupPolicy[0]));
            }
            if("ingress".equals(direction)){
                securityGroupPolicySet.setIngress(securityGroupPolicyList.toArray(new SecurityGroupPolicy[0]));
            }
            req.setSecurityGroupPolicySet(securityGroupPolicySet);

            VpcClient client = (VpcClient) abstractClient;
            return client.CreateSecurityGroupPolicies(req);
        }, region, this.getProductCode());
    }

    @Override
    public void revokeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        this.executeRevokeSecurityGroupPolicies(region, securityGroupId, permissions, "egress");
    }

    @Override
    public void revokeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        this.executeRevokeSecurityGroupPolicies(region, securityGroupId, permissions, "ingress");
    }

    /**
     *
     * @param region
     * @param securityGroupId
     * @param permissions
     * @param direction  访问类型。ingress（默认）：入方向。 egress：出方向。
     */
    private void executeRevokeSecurityGroupPolicies(String region, String securityGroupId,
                                                       List<SecurityGroupDTO.PermissionDTO> permissions,String direction) {
        client.handleClient((abstractClient) -> {

            DeleteSecurityGroupPoliciesRequest req = new DeleteSecurityGroupPoliciesRequest();
            req.setSecurityGroupId(securityGroupId);
            SecurityGroupPolicySet securityGroupPolicySet = new SecurityGroupPolicySet();

            List<SecurityGroupPolicy> securityGroupPolicyList = permissions.stream()
                    .map(permission -> {
                        SecurityGroupPolicy securityGroupPolicy = super.converter.convertM2P(permission, new SecurityGroupPolicy());
                        securityGroupPolicy.setPort(permission.getPortRange().replaceAll("/","-"));
                        return securityGroupPolicy;
                    }).toList();

            if("egress".equals(direction)){
                securityGroupPolicySet.setEgress(securityGroupPolicyList.toArray(new SecurityGroupPolicy[0]));
            }
            if("ingress".equals(direction)){
                securityGroupPolicySet.setIngress(securityGroupPolicyList.toArray(new SecurityGroupPolicy[0]));
            }
            req.setSecurityGroupPolicySet(securityGroupPolicySet);

            VpcClient client = (VpcClient) abstractClient;
            return client.DeleteSecurityGroupPolicies(req);
        }, region, this.getProductCode());
    }
}
