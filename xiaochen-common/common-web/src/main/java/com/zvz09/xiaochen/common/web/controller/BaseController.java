package com.zvz09.xiaochen.common.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zvz09.xiaochen.common.core.annotation.BizNo;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.common.web.entity.BaseEntity;
import com.zvz09.xiaochen.common.web.service.BaseService;
import com.zvz09.xiaochen.common.web.validation.UpdateValidation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Administrator
 */

@Setter
public abstract class BaseController<S extends BaseService<E>, E extends BaseEntity,D extends BaseDto<E>> {

    private S baseService;

    public BaseController(S baseService) {
        this.baseService = baseService;
    }

    @Operation(summary = "添加")
    @PostMapping()
    public void insert(@RequestBody D dto) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        E e = dto.convertedToPo();
        e.setId(null);
        baseService.save(e);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    @BizNo(spEl = "{#id}")
    public void delete(@PathVariable(value = "id") Long id) {
        baseService.removeById(id);
    }
    @Operation(summary = "批量删除")
    @DeleteMapping()
    @BizNo(spEl = "{#ids}")
    public void deleteBatch(@RequestBody List<Long> ids) {
        baseService.removeByIds(ids);
    }
    @PutMapping
    @Operation(summary = "修改")
    @BizNo(spEl = "{#dto.id}")
    public void update(@Validated(UpdateValidation.class) @RequestBody D dto) {
        baseService.updateById(dto.convertedToPo());
    }

    @PostMapping("/page")
    @Operation(summary = "列表")
    public <V> IPage<V> page(@RequestBody BasePage basePage) {
        return baseService.page(basePage);
    }

}
