/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.dto
 * @className com.zvz09.xiaochen.common.web.dto.BaseDto
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.dto;

import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * BaseDto
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/1 14:24
 */
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateValidation.class, message = "id不能为空")
    private String id;

    public Long getId() {
        return Long.valueOf(id);
    }
}
 