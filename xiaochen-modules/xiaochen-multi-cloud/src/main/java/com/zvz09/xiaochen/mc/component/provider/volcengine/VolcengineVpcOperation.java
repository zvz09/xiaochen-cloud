package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.volcengine.model.AbstractResponse;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.AuthorizeSecurityGroupEgressRequest;
import com.volcengine.vpc.model.AuthorizeSecurityGroupIngressRequest;
import com.volcengine.vpc.model.CreateSecurityGroupRequest;
import com.volcengine.vpc.model.CreateSecurityGroupResponse;
import com.volcengine.vpc.model.CreateSubnetRequest;
import com.volcengine.vpc.model.CreateSubnetResponse;
import com.volcengine.vpc.model.CreateVpcRequest;
import com.volcengine.vpc.model.CreateVpcResponse;
import com.volcengine.vpc.model.DeleteSecurityGroupRequest;
import com.volcengine.vpc.model.DeleteSubnetRequest;
import com.volcengine.vpc.model.DeleteVpcRequest;
import com.volcengine.vpc.model.DescribeSecurityGroupAttributesRequest;
import com.volcengine.vpc.model.DescribeSecurityGroupAttributesResponse;
import com.volcengine.vpc.model.DescribeSecurityGroupsRequest;
import com.volcengine.vpc.model.DescribeSecurityGroupsResponse;
import com.volcengine.vpc.model.DescribeSubnetsRequest;
import com.volcengine.vpc.model.DescribeSubnetsResponse;
import com.volcengine.vpc.model.DescribeVpcAttributesRequest;
import com.volcengine.vpc.model.DescribeVpcAttributesResponse;
import com.volcengine.vpc.model.DescribeVpcsRequest;
import com.volcengine.vpc.model.DescribeVpcsResponse;
import com.volcengine.vpc.model.RevokeSecurityGroupEgressRequest;
import com.volcengine.vpc.model.RevokeSecurityGroupIngressRequest;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VolcengineVpcOperation extends AbstractVpcOperation<VolcengineClient, AbstractResponse> implements VolcengineBaseOperation {

    private final IRegionService regionService;
    private final VolcengineEcsOperation ecsOperation;

    public VolcengineVpcOperation(VolcengineClient client, IRegionService regionService,
                                  VolcengineEcsOperation ecsOperation) {
        super(client, Integer.valueOf(100));
        this.regionService = regionService;
        this.ecsOperation = ecsOperation;
    }

    @Override
    protected AbstractResponse executeGetRegions(QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractResponse response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(AbstractResponse response, List<Region> regions) {
        regions.addAll(regionService.list(new LambdaQueryWrapper<Region>().eq(Region::getProviderCode,this.getProviderCode().getValue())));
    }

    @Override
    protected AbstractResponse executeGetZones(String region, QueryParameter queryParameter) {
        return null;
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractResponse response, List<ZoneDTO> zones) {
        zones.addAll(ecsOperation.listZones(region));
    }

    @Override
    public VpcInstance createVpc(VpcDTO vpcDTO) {
        CreateVpcResponse response = (CreateVpcResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            CreateVpcRequest request =  super.converter.convertM2P(vpcDTO,new CreateVpcRequest());

            return api.createVpc(request);
        }, vpcDTO.getRegion());
        return this.buildVpcInstance(response.getVpcId(), vpcDTO);
    }

    @Override
    public void deleteVpc(String region, String vpcId) {
        client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteVpcRequest request = new DeleteVpcRequest();
            request.setVpcId(vpcId);

            return api.deleteVpc(request);
        }, region);
    }

    @Override
    protected AbstractResponse executeDescribeVpcInstances(String region, QueryParameter queryParameter) {
        return (DescribeVpcsResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcsRequest request = new DescribeVpcsRequest();
            request.setMaxResults(super.maxPageSize);

            if(StringUtils.isNotBlank(queryParameter.getNextToken())){
                request.setNextToken(queryParameter.getNextToken());
            }
            return api.describeVpcs(request);
        },region);
    }

    @Override
    protected QueryParameter vpcInstancesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        return super.haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addVpcInstances(String region, AbstractResponse response, List<VpcInstance> instances) {
        DescribeVpcsResponse resp = (DescribeVpcsResponse) response;
        resp.getVpcs().stream().parallel().forEach(instance ->{
            instances.add(this.buildVpcInstance(region, this.executeDescribeVpcInstance(region,instance.getVpcId())));
        });
    }

    @Override
    protected VpcInstance buildVpcInstance(String region, AbstractResponse response) {
        DescribeVpcAttributesResponse resp = (DescribeVpcAttributesResponse) response;
        VpcInstance vpcInstance = super.converter.convertP2M(resp,new VpcInstance());
        vpcInstance.setProvider(this.getProviderCode().getValue());
        vpcInstance.setRegion(region);
        vpcInstance.setDetail(JSON.toJSONString(resp));
        return vpcInstance;
    }

    @Override
    protected AbstractResponse executeDescribeVpcInstance(String region, String instanceId) {
        return (DescribeVpcAttributesResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);
            DescribeVpcAttributesRequest request = new DescribeVpcAttributesRequest();
            request.setVpcId(instanceId);

            return api.describeVpcAttributes(request);
        },region);
    }

    @Override
    protected AbstractResponse executeGetVSwitches(String region, String vpcId, QueryParameter queryParameter) {
        return (DescribeSubnetsResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DescribeSubnetsRequest request = new DescribeSubnetsRequest();
            request.setPageNumber(queryParameter.getPageNumber());
            request.setPageSize(queryParameter.getPageSize());
            request.setVpcId(vpcId);
            return api.describeSubnets(request);
        }, region);
    }

    @Override
    protected void paddingVSwitchePage(String vpcId, AbstractResponse response, Page<VSwitcheDTO> page) {
        DescribeSubnetsResponse resp = (DescribeSubnetsResponse) response;
        page.setTotal(resp.getTotalCount());
        page.setRecords(resp.getSubnets().stream().map(vSwitch ->  super.converter.convertP2M(vSwitch,new VSwitcheDTO())).toList());
    }

    @Override
    public String createVSwitch(CreateVSwitch createVSwitch) {
        CreateSubnetResponse response = (CreateSubnetResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);
            CreateSubnetRequest request =  super.converter.convertM2P(createVSwitch,new CreateSubnetRequest()) ;
            return api.createSubnet(request);
        }, createVSwitch.getRegionId());
        return response.getSubnetId();
    }

    @Override
    public void deleteVSwitch(String region, String vSwitchId) {
        client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);

            DeleteSubnetRequest request = new DeleteSubnetRequest();
            request.setSubnetId(vSwitchId);

            return api.deleteSubnet(request);
        }, region);
    }


    @Override
    public AbstractResponse executeGetSecurityGroups(String region, QueryParameter queryParameter) {
        return  (DescribeSecurityGroupsResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);

            DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
            request.setPageNumber(queryParameter.getPageNumber());
            request.setPageSize(queryParameter.getPageSize());

            return api.describeSecurityGroups(request);
        },region);
    }

    @Override
    public void paddingSecurityGroupPage(AbstractResponse response, Page<SecurityGroupDTO> page) {
        DescribeSecurityGroupsResponse resp = (DescribeSecurityGroupsResponse) response;

        page.setTotal(resp.getTotalCount());
        List<SecurityGroupDTO> records = new ArrayList<>();

        resp.getSecurityGroups().forEach(securityGroup ->{
            records.add(super.converter.convertP2M(securityGroup,new SecurityGroupDTO()));
        });
        page.setRecords(records);
    }

    @Override
    public String createSecurityGroup(String region, SecurityGroupDTO securityGroupDTO) {
        CreateSecurityGroupResponse response = (CreateSecurityGroupResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);

            CreateSecurityGroupRequest request = super.converter.convertM2P(securityGroupDTO,new CreateSecurityGroupRequest()) ;
            request.setVpcId(securityGroupDTO.getVpcId());

            return api.createSecurityGroup(request);
        },region);
        return response.getSecurityGroupId();
    }

    @Override
    public void deleteSecurityGroup(String region, String securityGroupId) {
        client.handleClient((client)->{
            VpcApi api = new VpcApi(client);

            DeleteSecurityGroupRequest request = new DeleteSecurityGroupRequest();
            request.setSecurityGroupId(securityGroupId);

            return api.deleteSecurityGroup(request);
        },region);
    }

    @Override
    public AbstractResponse executeGetSecurityGroupAttributes(String region, String securityGroupId) {
        return (DescribeSecurityGroupAttributesResponse) client.handleClient((client)->{
            VpcApi api = new VpcApi(client);

            DescribeSecurityGroupAttributesRequest request = new DescribeSecurityGroupAttributesRequest();
            request.setSecurityGroupId(securityGroupId);

            return api.describeSecurityGroupAttributes(request);
        },region);
    }

    @Override
    public SecurityGroupDTO paddingSecurityGroupAttributes(AbstractResponse response) {
        DescribeSecurityGroupAttributesResponse resp = (DescribeSecurityGroupAttributesResponse) response;
        SecurityGroupDTO securityGroupDTO = super.converter.convertP2M(resp,new SecurityGroupDTO());
        List<SecurityGroupDTO.PermissionDTO> permissions = new ArrayList<>();
        resp.getPermissions().forEach(permission -> {
            SecurityGroupDTO.PermissionDTO permissionDTO = super.converter.convertP2M(permission,new SecurityGroupDTO.PermissionDTO());
            permissionDTO.setPortRange(String.format("%s/%s",permission.getPortStart(),permission.getPortEnd()));
            permissions.add(permissionDTO);
        });
        securityGroupDTO.setPermissions(permissions);
        return securityGroupDTO;
    }


    @Override
    public void authorizeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        permissions.parallelStream().forEach(permission -> {
            String[] ports = splitPortRange(permission);

            client.handleClient((client)->{
                VpcApi api = new VpcApi(client);

                AuthorizeSecurityGroupEgressRequest request = this.converter.convertM2P(permission,new AuthorizeSecurityGroupEgressRequest());
                request.setPortEnd(Integer.valueOf(ports[1]));
                request.setPortStart(Integer.valueOf(ports[0]));

                request.setSecurityGroupId(securityGroupId);
                return api.authorizeSecurityGroupEgress(request);
            },region);
        });
    }

    @Override
    public void authorizeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        permissions.parallelStream().forEach(permission -> {

            String[] ports = splitPortRange(permission);

            client.handleClient((client)->{
                VpcApi api = new VpcApi(client);

                AuthorizeSecurityGroupIngressRequest request =
                        super.converter.convertM2P(permission,new AuthorizeSecurityGroupIngressRequest());
                request.setPortEnd(Integer.valueOf(ports[1]));
                request.setPortStart(Integer.valueOf(ports[0]));

                request.setSecurityGroupId(securityGroupId);
                return api.authorizeSecurityGroupIngress(request);
            },region);
        });
    }

    @Override
    public void revokeSecurityGroupEgress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        permissions.parallelStream().forEach(permission -> {
            String[] ports = splitPortRange(permission);

            client.handleClient((client)->{
                VpcApi api = new VpcApi(client);

                RevokeSecurityGroupEgressRequest request =
                        super.converter.convertM2P(permission,new RevokeSecurityGroupEgressRequest());
                request.setPortEnd(Integer.valueOf(ports[1]));
                request.setPortStart(Integer.valueOf(ports[0]));

                request.setSecurityGroupId(securityGroupId);
                return api.revokeSecurityGroupEgress(request);
            },region);
        });
    }

    @Override
    public void revokeSecurityGroupIngress(String region, String securityGroupId, List<SecurityGroupDTO.PermissionDTO> permissions) {
        permissions.parallelStream().forEach(permission -> {
            String[] ports = splitPortRange(permission);

            client.handleClient((client)->{
                VpcApi api = new VpcApi(client);

                RevokeSecurityGroupIngressRequest request =
                        super.converter.convertM2P(permission,new RevokeSecurityGroupIngressRequest());
                request.setPortEnd(Integer.valueOf(ports[1]));
                request.setPortStart(Integer.valueOf(ports[0]));

                request.setSecurityGroupId(securityGroupId);
                return api.revokeSecurityGroupIngress(request);
            },region);
        });
    }

    private static String[] splitPortRange(SecurityGroupDTO.PermissionDTO permission) {
        if ("ICMP".equals(permission.getIpProtocol()) || "ALL".equals(permission.getIpProtocol())) {
            return new String[]{"-1","-1"};
        }
        return permission.getPortRange().contains("/") ?
                permission.getPortRange().split("/"):new String[]{permission.getPortRange(), permission.getPortRange()};
    }
}
