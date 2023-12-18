package com.zvz09.xiaochen.note.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zvz09.xiaochen.note.domain.entity.Article;
import com.zvz09.xiaochen.note.domain.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 博客文章表 Mapper 接口
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<ArticleVO> getPage(@Param("page") IPage<ArticleVO> page,
                             @Param(Constants.WRAPPER) Wrapper<Article> wrapper);
}
