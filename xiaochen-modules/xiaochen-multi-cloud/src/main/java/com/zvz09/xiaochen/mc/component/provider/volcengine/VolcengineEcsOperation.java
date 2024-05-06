package com.zvz09.xiaochen.mc.component.provider.volcengine;

import com.alibaba.fastjson.JSON;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.DescribeImagesRequest;
import com.volcengine.ecs.model.DescribeImagesResponse;
import com.volcengine.ecs.model.DescribeInstanceTypesRequest;
import com.volcengine.ecs.model.DescribeInstanceTypesResponse;
import com.volcengine.ecs.model.DescribeInstancesRequest;
import com.volcengine.ecs.model.DescribeInstancesResponse;
import com.volcengine.ecs.model.DescribeRegionsRequest;
import com.volcengine.ecs.model.DescribeRegionsResponse;
import com.volcengine.ecs.model.DescribeZonesRequest;
import com.volcengine.ecs.model.DescribeZonesResponse;
import com.volcengine.ecs.model.InstanceForDescribeInstancesOutput;
import com.volcengine.model.AbstractResponse;
import com.volcengine.vpc.VpcApi;
import com.volcengine.vpc.model.DescribeEipAddressAttributesRequest;
import com.volcengine.vpc.model.DescribeEipAddressAttributesResponse;
import com.zvz09.xiaochen.mc.component.product.ecs.AbstractEcsOperation;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class VolcengineEcsOperation extends AbstractEcsOperation<VolcengineClient, AbstractResponse> implements VolcengineBaseOperation {

    protected VolcengineEcsOperation(VolcengineClient client) {
        super(client, Integer.valueOf(100));
    }

    @Override
    protected AbstractResponse executeDescribeInstances(String region, QueryParameter queryParameter) {
        return (DescribeInstancesResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(this.maxPageSize);

            if (StringUtils.isNotBlank(queryParameter.getNextToken())) {
                describeInstancesRequest.setNextToken(queryParameter.getNextToken());
            }
            return api.describeInstances(describeInstancesRequest);
        }, region);
    }

    @Override
    protected AbstractResponse executeDescribeInstance(String region, String instanceId) {
        return  (DescribeInstancesResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setMaxResults(Integer.valueOf(1));
            describeInstancesRequest.setInstanceIds(List.of(instanceId));
            return api.describeInstances(describeInstancesRequest);
        }, region);
    }

    @Override
    protected QueryParameter ecsInstancesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeInstancesResponse resp = (DescribeInstancesResponse) response;
        return super.haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addEcsInstances(String region, AbstractResponse response, List<EcsInstance> instances) {
        DescribeInstancesResponse resp = (DescribeInstancesResponse) response;
        if(resp.getInstances()!=null){
            resp.getInstances().forEach(instance -> {
                instances.add(this.buildEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),instance.getOsName(),
                        region,instance.getCpus() + "C/" + instance.getMemorySize() + "MiB",
                        Objects.requireNonNull(executeDescribeEipAddressAttributes(region, instance.getEipAddress().getAllocationId())).getEipAddress(),
                        instance.getInstanceChargeType(),instance.getExpiredAt(),instance.getInstanceId()));
            });
        }

    }

    @Override
    protected EcsInstance buildEcsInstance(String region, AbstractResponse response) {
        DescribeInstancesResponse resp = (DescribeInstancesResponse) response;

        if (resp.getInstances() != null && !resp.getInstances().isEmpty()) {
            InstanceForDescribeInstancesOutput instance = resp.getInstances().get(0);
            return this.buildEcsInstance(instance.getInstanceId(),instance.getInstanceName(),instance.getStatus(),instance.getOsName(),
                    region,instance.getCpus() + "C/" + instance.getMemorySize() + "MiB",
                    Objects.requireNonNull(executeDescribeEipAddressAttributes(region, instance.getEipAddress().getAllocationId())).getEipAddress(),
                    instance.getInstanceChargeType(),instance.getExpiredAt(),instance.getInstanceId());
        }
        return null;
    }

    @Override
    protected AbstractResponse executeGetRegions(QueryParameter queryParameter) {
        return (DescribeRegionsResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
            describeRegionsRequest.setMaxResults(Integer.valueOf(20));
            if (StringUtils.isNotBlank(queryParameter.getNextToken())) {
                describeRegionsRequest.setNextToken(queryParameter.getNextToken());
            }
            return api.describeRegions(describeRegionsRequest);
        }, null);
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeRegionsResponse resp = (DescribeRegionsResponse) response;
        return super.haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addRegions(AbstractResponse response, List<Region> regions) {
        DescribeRegionsResponse resp = (DescribeRegionsResponse) response;
        resp.getRegions().forEach(region -> {
            regions.add(this.convertedRegion(region.getRegionId(), region.getRegionId(),null));
        });
    }

    @Override
    protected AbstractResponse executeGetZones(String region, QueryParameter queryParameter) {
        return (DescribeZonesResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeZonesRequest request = new DescribeZonesRequest();
            return api.describeZones(request);
        }, region);
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractResponse response, List<ZoneDTO> zones) {
        DescribeZonesResponse resp = (DescribeZonesResponse) response;
        resp.getZones().forEach(zone -> {
            new ZoneDTO(zone.getZoneId(), zone.getZoneId());
        });
    }

    @Override
    protected AbstractResponse executeGetEcsInstanceTypes(String region, QueryParameter queryParameter) {
        return (DescribeInstanceTypesResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeInstanceTypesRequest request = new DescribeInstanceTypesRequest();
            request.setMaxResults(Integer.valueOf(500));

            if (StringUtils.isNotBlank(queryParameter.getNextToken())) {
                request.setNextToken(queryParameter.getNextToken());
            }
            return api.describeInstanceTypes(request);
        }, region);
    }

    @Override
    protected QueryParameter ecsInstanceTypesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeInstanceTypesResponse resp = (DescribeInstanceTypesResponse) response;
        return super.haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addEcsInstanceTypes(String region, AbstractResponse response, List<EcsInstanceType> ecsInstanceTypes) {
        DescribeInstanceTypesResponse resp = (DescribeInstanceTypesResponse) response;
        resp.getInstanceTypes().forEach(output -> {
            ecsInstanceTypes.add(EcsInstanceType.builder()
                    .provider(this.getProviderCode().getValue())
                    .region(region)
                    .typeId(output.getInstanceTypeId())
                    .typeFamily(output.getInstanceTypeFamily())
                    .cpu(output.getProcessor().getCpus())
                    .cpuModel(output.getProcessor().getModel())
                    .cpuBaseFrequency(output.getProcessor().getBaseFrequency())
                    .cpuTurboFrequency(output.getProcessor().getTurboFrequency())
                    .memory(output.getMemory().getSize())
                    .localVolumes(JSON.toJSONString(output.getLocalVolumes()))
                    .volume(JSON.toJSONString(output.getVolume()))
                    .build());
        });
    }

    @Override
    protected AbstractResponse executeGetImages(String region, QueryParameter queryParameter) {
        return (DescribeImagesResponse) client.handleClient((client) -> {
            EcsApi api = new EcsApi(client);
            DescribeImagesRequest request = new DescribeImagesRequest();
            request.setMaxResults(Integer.valueOf(100));

            if (StringUtils.isNotBlank(queryParameter.getNextToken())) {
                request.setNextToken(queryParameter.getNextToken());
            }
            return api.describeImages(request);
        }, region);
    }

    @Override
    protected QueryParameter imagesHasNext(QueryParameter queryParameter, AbstractResponse response) {
        DescribeImagesResponse resp = (DescribeImagesResponse) response;
        return haveNext(queryParameter, resp.getNextToken());
    }

    @Override
    protected void addImages(String region, AbstractResponse response, List<ImageDTO> images) {
        DescribeImagesResponse resp = (DescribeImagesResponse) response;
        resp.getImages().forEach(output -> {
            ImageDTO imageDTO = this.converter.convertP2M(output,new ImageDTO());
            imageDTO.setRegion(region);
            images.add(imageDTO);
        });
    }

    private DescribeEipAddressAttributesResponse executeDescribeEipAddressAttributes(String region, String ipId) {
        if (StringUtils.isBlank(ipId)) {
            return null;
        }
        return (DescribeEipAddressAttributesResponse) client.handleClient((client) -> {
            VpcApi api = new VpcApi(client);
            DescribeEipAddressAttributesRequest describeEipAddressAttributesRequest = new DescribeEipAddressAttributesRequest();
            describeEipAddressAttributesRequest.setAllocationId(ipId);
            return api.describeEipAddressAttributes(describeEipAddressAttributesRequest);
        }, region);
    }

}
