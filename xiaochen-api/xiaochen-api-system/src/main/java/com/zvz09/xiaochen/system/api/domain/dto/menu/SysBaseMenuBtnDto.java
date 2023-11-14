package com.zvz09.xiaochen.system.api.domain.dto.menu;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysBaseMenuBtn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysBaseMenuBtn", description = "")
public class SysBaseMenuBtnDto extends BaseDto {

    private String name;

    private String description;

    /**
     * 转化为po对象
     *
     * @return SysBaseMenu
     */
    public SysBaseMenuBtn convertedToPo() {
        return SysBaseMenuBtn.builder()
                .id(this.getId())
                .name(this.name)
                .description(this.description)
                .build();
    }
}
