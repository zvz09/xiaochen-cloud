package com.zvz09.xiaochen.system.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_perm_code")
@Schema(description = "系统权限字")
public class SysPermCodeVo extends BaseVo {

    @Schema(description = "上级权限字Id")
    private String parentId;

    @Schema(description = "权限字标识(一般为有含义的英文字符串)")
    private String permCode;

    @Schema(description = "类型(0: 菜单 1: UI片段 2: 操作)")
    private String permCodeType;

    @Schema(description = "显示名称")
    private String showName;

    @Schema(description = "显示顺序(数值越小，越靠前)")
    private Integer showOrder;

    private List<String> apiIds;
    private List<SysApiVo> apiVos;

    @Schema(description = "子")
    private List<SysPermCodeVo> children;

    public SysPermCodeVo(SysPermCode sysPermCode) {
        super(sysPermCode.getId());
        this.parentId = String.valueOf(sysPermCode.getParentId());
        this.permCode = sysPermCode.getPermCode();
        this.permCodeType = sysPermCode.getPermCodeType();
        this.showName = sysPermCode.getShowName();
        this.showOrder = sysPermCode.getShowOrder();
    }

    public SysPermCodeVo(SysPermCode sysPermCode, List<Long> apiIds, List<SysApiVo> sysApiVos) {
        super(sysPermCode.getId());
        this.apiVos = sysApiVos;
        if (apiIds != null && !apiIds.isEmpty()) {
            this.apiIds = apiIds.stream()
                    .map(Object::toString)  // 使用toString方法将Long转换为String
                    .collect(Collectors.toList());
        } else {
            this.apiIds = new ArrayList<>();
        }
        this.parentId = String.valueOf(sysPermCode.getParentId());
        this.permCode = sysPermCode.getPermCode();
        this.permCodeType = sysPermCode.getPermCodeType();
        this.showName = sysPermCode.getShowName();
        this.showOrder = sysPermCode.getShowOrder();
    }
}
