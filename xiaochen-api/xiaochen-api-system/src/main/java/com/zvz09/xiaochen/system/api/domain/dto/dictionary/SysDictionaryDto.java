/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.dictionary
 * @className com.zvz09.xiaochen.system.dto.dictionary.SysDictionaryDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.dictionary;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SysDictionaryDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 10:53
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysDictionaryDto")
public class SysDictionaryDto extends BaseDto {
    @Schema(description = "字典名（中）")
    private String name;
    @Schema(description = "字典名（英）")
    private String type;
    @Schema(description = "状态")
    private Boolean status;
    @Schema(description = "描述")
    private String description;

    public SysDictionary convertedToPo() {
        return SysDictionary.builder()
                .id(this.getId())
                .name(this.name)
                .type(this.type)
                .status(this.status)
                .description(this.description)
                .build();
    }
}
 