package com.zvz09.xiaochen.system.api.domain.dto.menu;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 *
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysBaseMenuParameterDto", description = "")
public class SysBaseMenuParameterDto extends BaseDto {


    @Schema(description = "地址栏携带参数为params还是query")
    private String type;

    @Schema(description = "地址栏携带参数的key")
    private String key;

    @Schema(description = "地址栏携带参数值")
    private String value;

    /**
     * 转化为po对象
     *
     * @return SysBaseMenu
     */
    public SysBaseMenuParameter convertedToPo() {
        return SysBaseMenuParameter.builder()
                .id(this.getId())
                .type(this.getType())
                .parameterKey(this.getKey())
                .parameterValue(this.getValue())
                .build();
    }
}
