package com.zvz09.xiaochen.note.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 爬取数据类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("note_reptile_class")
@Schema(description =  "爬取数据类")
public class ReptileClass extends BaseEntity {

    @Schema(description ="类名")
    private String className;

    @Schema(description ="类内容")
    private String content;

}
