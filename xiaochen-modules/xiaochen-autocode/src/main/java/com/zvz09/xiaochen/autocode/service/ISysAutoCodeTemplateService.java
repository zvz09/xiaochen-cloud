package com.zvz09.xiaochen.autocode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.domain.dto.template.QueryDto;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;

import java.util.List;

/**
 * <p>
 * 系统自动代码模板 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-09-14
 */
public interface ISysAutoCodeTemplateService extends IService<SysAutoCodeTemplate> {

    Page<SysAutoCodeTemplate> getAutoCodeTemplateList(QueryDto queryDto);

    void createAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto);

    void deleteAutoCodeTemplateByIds(List<Long> ids);

    void deleteAutoCodeTemplate(Long id);

    void updateAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto);

    SysAutoCodeTemplate getAutoCodeTemplateDetail(Long id);
}
