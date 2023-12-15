/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.dictionary
 * @className com.zvz09.xiaochen.system.dto.dictionary.SysDictionaryDetailDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.dictionary;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionaryDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SysDictionaryDetailDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 16:46
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysDictionaryDetailDto")
public class SysDictionaryDetailDto extends BaseDto<SysDictionaryDetail> {

    @Schema(description = "展示值")
    private String label;
    @Schema(description = "字典值")
    private String value;
    @Schema(description = "启用状态")
    private Boolean status;
    @Schema(description = "启用状态")
    private Long sort;
    private String tagType;
    @Schema(description = "字典id")
    private Long sysDictionaryId;

    public SysDictionaryDetail convertedToPo() {
        return SysDictionaryDetail.builder()
                .id(this.getId())
                .label(this.label)
                .value(this.value)
                .status(this.status)
                .sort(this.sort)
                .tagType(this.tagType)
                .sysDictionaryId(this.sysDictionaryId)
                .build();
    }
}
 