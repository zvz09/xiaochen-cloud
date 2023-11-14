/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen
 * @className com.zvz09.xiaochen.Columns
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.dto.gencode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Columns
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 10:04
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Column {
    /**
     * 列描述
     */
    private String name;
    /**
     * 列描述
     */
    private String description;
    private String type;
    private Boolean dto;
    private Boolean canNull;
    private Boolean query;
    private String queryType;
    private Boolean sole;

    public Column(String name, String description, String type) {
        if (name != null) {
            this.name = name.replaceAll("`", "");
        }
        this.description = description;
        this.type = type;
        this.dto = false;
        this.query = false;
        this.queryType = "";
        this.sole = false;
    }

    public String getCapName() {
        return StringUtils.capitalize(name);
    }
}
 