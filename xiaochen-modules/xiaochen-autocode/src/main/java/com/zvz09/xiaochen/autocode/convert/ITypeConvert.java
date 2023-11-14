/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen
 * @className com.zvz09.xiaochen.ITypeConvert
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.convert;

import net.sf.jsqlparser.statement.create.table.ColDataType;

/**
 * 数据库字段类型转换
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/20 13:18
 */
public interface ITypeConvert {
    DbColumnType processTypeConvert(ColDataType colDataType);
}