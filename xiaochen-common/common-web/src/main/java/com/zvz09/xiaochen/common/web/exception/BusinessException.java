/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.exception
 * @className com.zvz09.xiaochen.common.web.exception.BusinessException
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.exception;

import com.zvz09.xiaochen.common.web.enums.ReturnCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BusinessException
 *
 * @author zvz09
 * @version 1.0
 * @description 业务异常
 * @date 2023/8/25 16:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(ReturnCode code) {
        super(code.getMessage());
        this.setCode(code.getCode());
    }

    public BusinessException(String msg) {
        super(msg);
        this.setCode(ReturnCode.SYSTEM_ERROR.getCode());
    }
}
 