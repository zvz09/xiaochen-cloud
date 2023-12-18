package com.zvz09.xiaochen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.blog.domain.entity.Tags;
import com.zvz09.xiaochen.blog.mapper.TagsMapper;
import com.zvz09.xiaochen.blog.service.ITagsService;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    @Override
    public IPage<Tags> listTags(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()),
                new LambdaQueryWrapper<Tags>()
                        .like(StringUtils.isNotBlank(basePage.getKeyword()), Tags::getName, basePage.getKeyword()));
    }

    @Override
    public void insertTag(Tags tags) {
        if (this.count(new LambdaQueryWrapper<Tags>().eq(Tags::getName, tags.getName())) > 0) {
            throw new BusinessException("该分类名称已存在");
        }
        this.save(tags);
    }

    @Override
    public Tags getTagsById(Long id) {
        return this.getById(id);
    }

    @Override
    public void updateTag(Tags tags) {
        if (this.getById(tags.getId()) != null) {
            this.updateById(tags);
        }
    }

    @Override
    public void deleteById(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.removeBatchByIds(ids);
        }
    }

    @Override
    public void top(Long id) {
        Tags tags = baseMapper.selectOne(new LambdaQueryWrapper<Tags>().orderByDesc(Tags::getSort).last("LIMIT 1"));
        if (!tags.getId().equals(id)) {
            Tags entity = Tags.builder()
                    .sort(tags.getSort() + 1).id(id).build();
            this.updateById(entity);
        }
    }
}
