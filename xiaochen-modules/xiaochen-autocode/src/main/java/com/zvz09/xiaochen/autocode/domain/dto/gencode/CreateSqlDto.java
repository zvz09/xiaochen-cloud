/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode.dto.gencode
 * @className com.zvz09.xiaochen.autocode.dto.gencode.CreateSqlDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.dto.gencode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * CreateSqlDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 15:32
 */
@Getter
@Setter
@Schema(name = "建表sql", description = "")
public class CreateSqlDto {
    @NotBlank(message = "sql 不能为空")
    private String sql;
    @NotBlank(message = "数据库类型不能为空")
    private String dbType;
}
 