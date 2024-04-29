package com.zvz09.xiaochen.mc.component.provider;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.mc.domain.dto.QueryParameter;
import com.zvz09.xiaochen.mc.domain.dto.ZoneDTO;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBaseOperation<C,R> implements BaseProductService  {

    protected final C client;

    protected final Integer maxPageSize;

    protected AbstractBaseOperation(C client, Integer maxPageSize) {
        this.client = client;
        this.maxPageSize = maxPageSize;
    }

    /**
     * 查询云服务商上所有地域
     * @return
     */
    public List<Region> listRegions(){
        List<Region> regions = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(1).build();
        R response = this.executeGetRegions(queryParameter);

        //处理请求
        this.addRegions(response,regions);

        while ((queryParameter = this.regionsHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeGetRegions(queryParameter);
            this.addRegions(response,regions);
        }

        return regions;
    }

    /**
     * 执行请求云服务商 地域列表接口
     * @param queryParameter
     * @return
     */
    protected abstract R executeGetRegions(QueryParameter queryParameter);

    /**
     * 根据云服务商返回结果判断是否还有下一页
     * @param queryParameter
     * @param response
     * @return
     */
    protected abstract QueryParameter regionsHasNext(QueryParameter queryParameter, R response);

    /**
     * 将云服务商返回结果 转为 DTO 集合
     * @param response
     * @return
     */
    protected abstract void addRegions(R response, List<Region> regions);

    public Region convertedRegion(String regionCode, String regionName, String endpoint) {
        return Region.builder()
                .providerCode(this.getProviderCode().getValue())
                .productCode(this.getProductCode().name())
                .regionCode(regionCode)
                .regionName(regionName)
                .endpoint(endpoint)
                .build();
    }


    public List<ZoneDTO> listZones(String region){
        List<ZoneDTO> zones = new ArrayList<>();
        QueryParameter queryParameter = QueryParameter.builder().pageSize(maxPageSize).pageNumber(1).build();
        R response = this.executeGetZones(region,queryParameter);

        //处理请求
        this.addZoneDTOs(region,response,zones);

        while ((queryParameter = this.zonesHasNext(queryParameter,response)).isHaveNext()){
            response = this.executeGetZones(region,queryParameter);
            this.addZoneDTOs(region,response,zones);
        }

        return zones;
    }

    protected abstract R executeGetZones(String region,QueryParameter queryParameter);

    protected abstract QueryParameter zonesHasNext(QueryParameter queryParameter, R response);

    protected abstract void addZoneDTOs(String region, R response, List<ZoneDTO> zones);

    protected QueryParameter haveNext(QueryParameter queryParameter,String nextToken) {
        if(queryParameter == null){
            throw new BusinessException("queryParameter 不能为空");
        }
        if(StringUtils.isNotBlank(nextToken)){
            queryParameter.setHaveNext(true);
            queryParameter.setNextToken(nextToken);
        }else {
            queryParameter.setHaveNext(false);
        }
        return queryParameter;
    }
    protected QueryParameter haveNext(QueryParameter queryParameter,Long TotalCount) {
        if(queryParameter == null){
            throw new BusinessException("queryParameter 不能为空");
        }
        if(queryParameter.getOffset() + queryParameter.getPageSize() < TotalCount){
            queryParameter.setHaveNext(true);
            queryParameter.setPageNumber(queryParameter.getPageNumber() +1);
        }else {
            queryParameter.setHaveNext(false);
        }
        return queryParameter;
    }
}
