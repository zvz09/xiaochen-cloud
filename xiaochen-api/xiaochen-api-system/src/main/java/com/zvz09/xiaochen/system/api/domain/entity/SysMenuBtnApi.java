package com.zvz09.xiaochen.system.api.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * default description 实体类
 *
 * @author Default Author
 * @date 2023-11-16 10:10:30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("sys_menu_btn_api")
@Schema(name = "SysMenuBtnApi", description = "菜单按钮与API关联表")
public class SysMenuBtnApi{

    @Schema(description = "")
    private Long menuBtnId;
    @Schema(description = "")
    private Long apiId;
}

