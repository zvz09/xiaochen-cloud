/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.vo
 * @className com.zvz09.xiaochen.system.vo.SysApiVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SysApiVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/31 17:52
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysApisVo")
public class SysApiVo extends BaseVo {

    private String path;

    private String description;

    private String apiGroup;

    private String method;

    @Schema(description = "子")
    private List<SysApiVo> children;

    public SysApiVo(SysApi sysApi) {
        super(sysApi.getId());
        this.path = sysApi.getPath();
        this.description = sysApi.getDescription();
        this.apiGroup = sysApi.getApiGroup();
        this.method = sysApi.getMethod();
    }
}
 