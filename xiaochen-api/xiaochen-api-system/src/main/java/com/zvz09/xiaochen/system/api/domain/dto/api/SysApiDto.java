package com.zvz09.xiaochen.system.api.domain.dto.api;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
@Schema(name = "接口信息", description = "")
public class SysApiDto extends BaseDto<SysApi> {

    @Schema(description = "api路径")
    @NotBlank(message = "api路径不能为空")
    private String path;

    @Schema(description = "api中文描述")
    @NotBlank(message = "api中文描述不能为空")
    private String description;

    @Schema(description = "api分组")
    @NotBlank(message = "api分组不能为空")
    private String apiGroup;

    @Schema(description = "请求方式")
    @NotBlank(message = "请求方式不能为空")
    private String method;

    /**
     * 转化为po对象
     *
     * @return SysApi
     */
    public SysApi convertedToPo() {
        return SysApi.builder().id(this.getId())
                .path(this.path)
                .description(this.description)
                .apiGroup(this.apiGroup)
                .method(this.method).build();
    }
}
