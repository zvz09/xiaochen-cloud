/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.system.dto.api
 * @className com.zvz09.xiaochen.system.dto.api.SysApiQuery
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.system.api.domain.dto.operationRecord;

import com.zvz09.xiaochen.common.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SysApiQuery
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/12 13:46
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysOperationRecordQuery extends BasePage {

    @Schema(description = "请求方法")
    private String method;
    @Schema(description = "请求路径")
    private String path;
    @Schema(description = "结果状态码")
    private Long status;
}
 