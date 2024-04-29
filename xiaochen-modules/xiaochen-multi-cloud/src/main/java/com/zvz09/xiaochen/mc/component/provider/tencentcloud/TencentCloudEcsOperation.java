package com.zvz09.xiaochen.mc.component.provider.tencentcloud;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeImagesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeImagesResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstanceTypeConfigsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstanceTypeConfigsResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.cvm.v20170312.models.RegionInfo;
import com.tencentcloudapi.cvm.v20170312.models.ZoneInfo;
import com.zvz09.xiaochen.mc.component.provider.AbstractEcsOperation;
import com.zvz09.xiaochen.mc.domain.dto.ImageDTO;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstance;
import com.zvz09.xiaochen.mc.domain.entity.EcsInstanceType;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TencentCloudEcsOperation extends AbstractEcsOperation<TencentCloudClient, AbstractModel> implements TencentCloudBaseOperation {


    protected TencentCloudEcsOperation(TencentCloudClient client) {
        super(client, 100);
    }

    @Override
    protected AbstractModel executeDescribeInstances(String region, QueryParameter queryParameter) {
        return (DescribeInstancesResponse) client.handleClient((abstractClient)->{
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            req.setOffset(Long.valueOf(queryParameter.getOffset()));
            req.setLimit(Long.valueOf(queryParameter.getPageSize()));
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstances(req);
        },region, this.getProductCode());
    }

    @Override
    protected AbstractModel executeDescribeInstance(String region, String instanceId) {
        return (DescribeInstancesResponse) client.handleClient((abstractClient)->{
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            req.setOffset(0L);
            req.setLimit(1L);
            req.setInstanceIds(new String[]{instanceId});
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstances(req);
        },region, this.getProductCode());
    }

    @Override
    protected QueryParameter ecsInstancesHasNext(QueryParameter queryParameter, AbstractModel response) {
        DescribeInstancesResponse resp = (DescribeInstancesResponse) response;

        return super.haveNext(queryParameter,resp.getTotalCount());
    }

    @Override
    protected void addEcsInstances(String region, AbstractModel response, List<EcsInstance> instances) {
        DescribeInstancesResponse describeInstancesResponse = (DescribeInstancesResponse) response;
        for (Instance instance : describeInstancesResponse.getInstanceSet()){
            instances.add(this.buildEcsInstance(instance.getInstanceId(), instance.getInstanceName(), instance.getInstanceState(),instance.getOsName(),
                    region,instance.getCPU() + "C/" + instance.getMemory() + "GB",JSON.toJSONString(instance.getPublicIpAddresses())
                    ,instance.getInstanceChargeType(),instance.getExpiredTime(),JSON.toJSONString(instance)));
        }
    }

    @Override
    protected EcsInstance buildEcsInstance(String region, AbstractModel response) {
        DescribeInstancesResponse describeInstancesResponse = (DescribeInstancesResponse) response;
        if(describeInstancesResponse.getInstanceSet() !=null && describeInstancesResponse.getInstanceSet().length > 0){
            Instance instance = describeInstancesResponse.getInstanceSet()[0];
            return this.buildEcsInstance(instance.getInstanceId(), instance.getInstanceName(), instance.getInstanceState(),instance.getOsName(),
                    region,instance.getCPU() + "C/" + instance.getMemory() + "GB",JSON.toJSONString(instance.getPublicIpAddresses())
                    ,instance.getInstanceChargeType(),instance.getExpiredTime(),JSON.toJSONString(instance));
        }
        return null;
    }

    @Override
    protected AbstractModel executeGetRegions(QueryParameter queryParameter) {
        return (DescribeRegionsResponse) client.handleClient((abstractClient)->{
            DescribeRegionsRequest req = new DescribeRegionsRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeRegions(req);
        },null, this.getProductCode());
    }

    @Override
    protected QueryParameter regionsHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addRegions(AbstractModel response, List<Region> regions) {
        DescribeRegionsResponse describeRegionsResponse = (DescribeRegionsResponse) response;
        for (RegionInfo regionInfo : describeRegionsResponse.getRegionSet()){
            regions.add(this.convertedRegion(regionInfo.getRegion(), regionInfo.getRegionName(),null));
        }
    }

    @Override
    protected AbstractModel executeGetZones(String region, QueryParameter queryParameter) {
        return (DescribeZonesResponse) client.handleClient((abstractClient) -> {
            DescribeZonesRequest req = new DescribeZonesRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeZones(req);
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter zonesHasNext(QueryParameter queryParameter, AbstractModel response) {
        queryParameter.setHaveNext(false);
        return queryParameter;
    }

    @Override
    protected void addZoneDTOs(String region, AbstractModel response, List<ZoneDTO> zones) {
        DescribeZonesResponse describeZonesResponse = (DescribeZonesResponse) response;
        if (describeZonesResponse.getZoneSet() != null) {
            for (ZoneInfo zone : describeZonesResponse.getZoneSet()) {
                zones.add(new ZoneDTO(zone.getZone(), zone.getZoneName()));
            }
        }
    }

    @Override
    protected AbstractModel executeGetEcsInstanceTypes(String region, QueryParameter queryParameter) {
        return (DescribeInstanceTypeConfigsResponse) client.handleClient((abstractClient) -> {
            DescribeInstanceTypeConfigsRequest req = new DescribeInstanceTypeConfigsRequest();
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeInstanceTypeConfigs(req);
        }, region, this.getProductCode());
    }

    @Override
    protected QueryParameter ecsInstanceTypesHasNext(QueryParameter queryParameter, AbstractModel response) {
       queryParameter.setHaveNext(false);
       return queryParameter;
    }

    @Override
    protected void addEcsInstanceTypes(String region, AbstractModel response, List<EcsInstanceType> ecsInstanceTypes) {
        DescribeInstanceTypeConfigsResponse resp = (DescribeInstanceTypeConfigsResponse) response;
        if (resp.getInstanceTypeConfigSet() != null) {
            Arrays.stream(resp.getInstanceTypeConfigSet()).forEach(instanceType -> {
                ecsInstanceTypes.add(this.buildEcsInstanceType(region,instanceType.getInstanceType(),instanceType.getInstanceFamily(),
                        Math.toIntExact(instanceType.getCPU()),
                        null,null,null,Math.toIntExact(instanceType.getMemory()),null,null));
            });
        }
    }

    @Override
    protected AbstractModel executeGetImages(String region, QueryParameter queryParameter) {
        return (DescribeImagesResponse) client.handleClient((abstractClient)->{
            DescribeImagesRequest req = new DescribeImagesRequest ();
            req.setOffset(Long.valueOf(queryParameter.getOffset()));
            req.setLimit(Long.valueOf(queryParameter.getPageSize()));
            CvmClient cvmClient = (CvmClient) abstractClient;
            return cvmClient.DescribeImages(req);
        },region, this.getProductCode());
    }

    @Override
    protected QueryParameter imagesHasNext(QueryParameter queryParameter, AbstractModel response) {
        DescribeImagesResponse resp = (DescribeImagesResponse) response;

        return super.haveNext(queryParameter,resp.getTotalCount());
    }

    @Override
    protected void addImages(String region, AbstractModel response, List<ImageDTO> images) {
        DescribeImagesResponse res = (DescribeImagesResponse) response;
        if(res.getImageSet() != null){
            Arrays.stream(res.getImageSet()).filter(image -> "NORMAL".equals(image.getImageState())).forEach(image -> {
                images.add(this.buildImageDTO(region,image.getArchitecture(),null,image.getImageDescription(),
                        image.getImageId(),image.getImageName(),image.getIsSupportCloudinit(),image.getOsName(),null,
                        image.getPlatform(),null,Math.toIntExact(image.getImageSize()),image.getImageState()));
            });
        }
    }
}
