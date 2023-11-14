package com.zvz09.xiaochen.system.api.domain.vo;

import com.zvz09.xiaochen.common.web.vo.BaseVo;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionaryDetail;
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
@Schema(name = "SysDictionaryDetail", description = "")
public class SysDictionaryDetailVo extends BaseVo {

    private String label;

    private String value;

    private Boolean status;

    private Long sort;

    private Long sysDictionaryId;

    public SysDictionaryDetailVo(SysDictionaryDetail sysDictionaryDetail) {
        super(sysDictionaryDetail.getId());
        this.label = sysDictionaryDetail.getLabel();
        this.value = sysDictionaryDetail.getValue();
        this.status = sysDictionaryDetail.getStatus();
        this.sort = sysDictionaryDetail.getSort();
        this.sysDictionaryId = sysDictionaryDetail.getSysDictionaryId();
    }
}
