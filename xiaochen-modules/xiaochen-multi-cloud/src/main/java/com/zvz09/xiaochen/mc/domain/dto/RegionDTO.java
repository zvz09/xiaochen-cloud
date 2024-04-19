package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.mc.domain.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 地域表
 * @TableName mcmp_region
 */
@Getter
@Setter
public class RegionDTO extends BaseDto<Region> {



    @Schema(description ="云厂商code")
    @NotBlank(message = "云厂商code不能为空")
    private String providerCode;


    @Schema(description ="产品编码")
    @NotBlank(message = "产品编码不能为空")
    private String productCode;


    @Schema(description ="地域编码")
    @NotBlank(message = "地域编码不能为空")
    private String regionCode;


    @Schema(description ="地域名称")
    @NotBlank(message = "地域名称不能为空")
    private String regionName;

    @Override
    public Region convertedToPo() {
        return Region.builder()
                .providerCode(this.providerCode)
                .productCode(this.productCode)
                .regionCode(this.regionCode)
                .regionName(this.regionName)
                .build();
    }
}
