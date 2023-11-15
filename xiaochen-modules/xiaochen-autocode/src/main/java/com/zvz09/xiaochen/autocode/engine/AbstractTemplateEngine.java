package com.zvz09.xiaochen.autocode.engine;

import com.zvz09.xiaochen.autocode.domain.dto.gencode.Column;
import com.zvz09.xiaochen.autocode.domain.dto.gencode.GenConfig;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zvz09
 */
public abstract class AbstractTemplateEngine {

    protected Map<String, Object> buildParams(GenConfig genConfig) {
        Map<String, Object> params = new HashMap<>();
        params.put("genConfig", genConfig);
        params.put("columns", getColumns(genConfig));
        params.put("queryColumns", getQueryColumns(genConfig));
        params.put("dtoColumns", getDtoColumns(genConfig));
        params.put("soleColumns", getSoleColumns(genConfig));
        return params;
    }

    protected List<Column> getColumns(GenConfig genConfig) {
        return genConfig.getColumns();
    }

    protected List<Column> getQueryColumns(GenConfig genConfig) {
        return genConfig.getColumns().stream().filter(Column::getQuery).collect(Collectors.toList());
    }

    protected List<Column> getDtoColumns(GenConfig genConfig) {
        return genConfig.getColumns().stream().filter(Column::getDto).collect(Collectors.toList());
    }

    protected List<Column> getSoleColumns(GenConfig genConfig) {
        return genConfig.getColumns().stream().filter(Column::getSole).collect(Collectors.toList());
    }

    protected StringWriter stringToStringWriter(String str) {
        StringWriter sw = new StringWriter();
        sw.write(str);
        return sw;
    }
}
