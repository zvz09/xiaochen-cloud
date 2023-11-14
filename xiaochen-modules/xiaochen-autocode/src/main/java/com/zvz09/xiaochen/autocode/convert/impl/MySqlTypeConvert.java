/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen
 * @className com.zvz09.xiaochen.MySqlTypeConvert
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.convert.impl;

import com.zvz09.xiaochen.autocode.convert.DbColumnType;
import com.zvz09.xiaochen.autocode.convert.ITypeConvert;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * MySqlTypeConvert
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 13:31
 */
@Component
public class MySqlTypeConvert implements ITypeConvert {

    private static final Map<String, Function<List<String>, DbColumnType>> ConvertMap = new HashMap<>();

    static {

        ConvertMap.put("char", argumentsStringList -> DbColumnType.STRING);
        ConvertMap.put("text", argumentsStringList -> DbColumnType.STRING);
        ConvertMap.put("json", argumentsStringList -> DbColumnType.STRING);
        ConvertMap.put("enum", argumentsStringList -> DbColumnType.STRING);
        ConvertMap.put("bigint", argumentsStringList -> DbColumnType.LONG);
        ConvertMap.put("bit", argumentsStringList -> {
            if (argumentsStringList != null && argumentsStringList.contains("1")) {
                return DbColumnType.BOOLEAN;
            } else {
                return DbColumnType.BYTE;
            }
        });
        ConvertMap.put("tinyint", argumentsStringList -> {
            if (argumentsStringList != null && argumentsStringList.contains("1")) {
                return DbColumnType.BOOLEAN;
            } else {
                return DbColumnType.STRING;
            }
        });
        ConvertMap.put("int", argumentsStringList -> DbColumnType.INTEGER);
        ConvertMap.put("decimal", argumentsStringList -> DbColumnType.BIG_DECIMAL);
        ConvertMap.put("clob", argumentsStringList -> DbColumnType.CLOB);
        ConvertMap.put("blob", argumentsStringList -> DbColumnType.BLOB);
        ConvertMap.put("binary", argumentsStringList -> DbColumnType.BYTE_ARRAY);
        ConvertMap.put("float", argumentsStringList -> DbColumnType.FLOAT);
        ConvertMap.put("double", argumentsStringList -> DbColumnType.DOUBLE);
        ConvertMap.put("date", argumentsStringList -> DbColumnType.LOCAL_DATE);
        ConvertMap.put("time", argumentsStringList -> DbColumnType.LOCAL_TIME);
        ConvertMap.put("year", argumentsStringList -> DbColumnType.YEAR);
    }

    @Override
    public DbColumnType processTypeConvert(ColDataType colDataType) {
        Function<List<String>, DbColumnType> function = ConvertMap.get(colDataType.getDataType());
        if (function == null) {
            return DbColumnType.STRING;
        }
        return function.apply(colDataType.getArgumentsStringList());
    }
}
 