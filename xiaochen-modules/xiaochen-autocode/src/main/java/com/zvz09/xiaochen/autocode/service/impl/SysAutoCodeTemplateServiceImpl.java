package com.zvz09.xiaochen.autocode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.autocode.domain.dto.template.AutoCodeTemplateDto;
import com.zvz09.xiaochen.autocode.domain.dto.template.QueryDto;
import com.zvz09.xiaochen.autocode.domain.entity.SysAutoCodeTemplate;
import com.zvz09.xiaochen.autocode.mapper.SysAutoCodeTemplateMapper;
import com.zvz09.xiaochen.autocode.service.ISysAutoCodeTemplateService;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统自动代码模板 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-09-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAutoCodeTemplateServiceImpl extends ServiceImpl<SysAutoCodeTemplateMapper, SysAutoCodeTemplate> implements ISysAutoCodeTemplateService {
    @Override
    public Page<SysAutoCodeTemplate> getAutoCodeTemplateList(QueryDto queryDto) {
        return this.page(new Page<>(queryDto.getPageNum(), queryDto.getPageSize()),
                new LambdaQueryWrapper<SysAutoCodeTemplate>()
                        .eq(StringUtils.isNotBlank(queryDto.getLanguage()), SysAutoCodeTemplate::getLanguage, queryDto.getLanguage())
                        .eq(StringUtils.isNotBlank(queryDto.getTemplateEngine()), SysAutoCodeTemplate::getTemplateEngine, queryDto.getTemplateEngine())
                        .like(StringUtils.isNotBlank(queryDto.getName()), SysAutoCodeTemplate::getName, queryDto.getName()));
    }

    @Override
    public void createAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto) {
        if (this.count(new LambdaQueryWrapper<SysAutoCodeTemplate>()
                .eq(SysAutoCodeTemplate::getName, autoCodeTemplateDto.getName())) > 0) {
            throw new BusinessException("模板名已存在");
        }
        this.save(autoCodeTemplateDto.convertedToPo());
    }

    @Override
    public void deleteAutoCodeTemplateByIds(List<Long> ids) {
        if (ids == null || !ids.isEmpty()) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysAutoCodeTemplate>().in(SysAutoCodeTemplate::getId, ids)
                .set(SysAutoCodeTemplate::getDeleted, true));
    }

    @Override
    public void deleteAutoCodeTemplate(Long id) {
        if (id == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysAutoCodeTemplate>().eq(SysAutoCodeTemplate::getId, id)
                .set(SysAutoCodeTemplate::getDeleted, true));
    }

    @Override
    public void updateAutoCodeTemplate(AutoCodeTemplateDto autoCodeTemplateDto) {
        if (this.count(new LambdaQueryWrapper<SysAutoCodeTemplate>()
                .eq(SysAutoCodeTemplate::getName, autoCodeTemplateDto.getName())
                .ne(SysAutoCodeTemplate::getId, autoCodeTemplateDto.getId())) > 0) {
            throw new BusinessException("模板名已存在");
        }
        this.updateById(autoCodeTemplateDto.convertedToPo());
    }

    @Override
    public SysAutoCodeTemplate getAutoCodeTemplateDetail(Long id) {
        return this.getById(id);
    }
}
