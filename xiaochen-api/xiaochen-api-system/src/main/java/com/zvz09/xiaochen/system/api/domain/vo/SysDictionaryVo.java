package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Getter
@Setter
@Schema(name = "SysDictionary", description = "")
public class SysDictionaryVo extends BaseVo {

    private String name;

    private String type;

    private Boolean status;

    private String description;

    public SysDictionaryVo(SysDictionary sysDictionary) {
        super(sysDictionary.getId());
        this.name = sysDictionary.getName();
        this.type = sysDictionary.getType();
        this.status = sysDictionary.getStatus();
        this.description = sysDictionary.getDescription();
    }
}
