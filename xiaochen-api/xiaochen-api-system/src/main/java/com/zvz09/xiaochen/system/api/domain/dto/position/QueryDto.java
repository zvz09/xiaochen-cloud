package com.zvz09.xiaochen.system.api.domain.dto.position;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AutoCodeTemplateQuery
 *
 * @author zvz09
 * @version 1.0
 * @genConfig.description
 * @date 2023/9/14 14:40
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "position$QueryDto")
public class QueryDto extends BasePage {
    @Schema(description = "岗位编码")
    private String positionCode;
    @Schema(description = "岗位名称")
    private String positionName;
}
