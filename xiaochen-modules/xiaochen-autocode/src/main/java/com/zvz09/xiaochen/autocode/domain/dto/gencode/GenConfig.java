/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen
 * @className com.zvz09.xiaochen.Table
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.dto.gencode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Table
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 10:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenConfig {
    //基础包名
    private String basePackageName = "default.base.package";
    //业务描述
    private String description = "default description";
    //作者
    private String author = "Default Author";
    //时间
    private String datetime = "2022-10-28";
    //表名
    private String tableName = "default_table_name";
    //实体类名称
    private String className = "defaultTableName";
    //业务子包名
    private String businessName = "default";
    //实体类简称
    private String classShortName = "default";

    public List<Column> columns;

    private List<Long> templateIds;

    public String getCapClassShortName() {
        return StringUtils.capitalize(classShortName);
    }

    public String getCapClassName() {
        return StringUtils.capitalize(className);
    }

    public String getCapBusinessName() {
        return StringUtils.capitalize(businessName);
    }
}
 