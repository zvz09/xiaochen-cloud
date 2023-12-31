package com.zvz09.xiaochen.system.api.domain.dto.perm;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * <p>
 * 系统权限字
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_perm_code")
@Schema(description = "系统权限字")
public class SysPermCodeDto extends BaseDto<SysPermCode> {

    @Schema(description = "上级权限字Id")
    private Long parentId;

    @Schema(description = "权限字标识(一般为有含义的英文字符串)")
    private String permCode;

    @Schema(description = "类型(0: 菜单 1: UI片段 2: 操作)")
    private String permCodeType;

    @Schema(description = "显示名称")
    private String showName;

    @Schema(description = "显示顺序(数值越小，越靠前)")
    private Integer showOrder;

    @Override
    public SysPermCode convertedToPo() {
        return SysPermCode.builder()
                .id(this.getId())
                .parentId(this.parentId)
                .permCode(this.permCode)
                .permCodeType(this.permCodeType)
                .showName(this.showName)
                .showOrder(this.showOrder)
                .build();
    }
}
