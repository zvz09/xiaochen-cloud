package com.zvz09.xiaochen.system.api.domain.dto.dept;

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
@Schema(name = "dept$QueryDto")
public class QueryDto extends BasePage {
    @Schema(description = "部门名称")
    private String deptName;
}
