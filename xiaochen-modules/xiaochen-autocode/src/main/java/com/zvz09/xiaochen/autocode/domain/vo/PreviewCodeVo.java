/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.autocode
 * @className com.zvz09.xiaochen.autocode.vo.PreviewCodeVo
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.autocode.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * PreviewCodeVo
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/21 10:10
 */
@Getter
@Setter
public class PreviewCodeVo {

    private String title;
    private String name;
    private String type;
    private String code;

    public PreviewCodeVo(String title, String code) {
        this.title = title;
        this.name = title;
        this.code = code;
        this.type = title.substring(title.lastIndexOf(".") + 1);
    }
}
 