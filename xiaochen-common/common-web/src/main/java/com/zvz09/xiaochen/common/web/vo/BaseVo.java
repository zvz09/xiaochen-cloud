/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.vo
 * @className com.zvz09.xiaochen.common.web.vo.BaseVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * BaseVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/1 10:33
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class) //雪花算法id 返给前端转为 string类型
    private Long id;
}
 