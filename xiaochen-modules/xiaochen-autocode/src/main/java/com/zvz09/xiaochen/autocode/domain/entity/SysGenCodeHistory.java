package com.zvz09.xiaochen.autocode.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 历史记录 实体类
 *
 * @author zvz09
 * @date 2023-10-11 10:42:55
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_gencode_history")
@Schema(name = "SysGenCodeHistory", description = "历史记录")
public class SysGenCodeHistory extends BaseEntity {

    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "业务描述")
    private String description;
    @Schema(description = "配置信息json")
    private String genConfig;
}
