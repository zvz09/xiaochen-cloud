/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.entity
 * @className com.zvz09.xiaochen.common.web.entity.BaseEntity
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/31 15:11
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class) //雪花算法id 返给前端转为 string类型
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8", shape = JsonFormat.Shape.STRING) //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 序列
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8", shape = JsonFormat.Shape.STRING) //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 序列
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
 