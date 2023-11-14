package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionaryDetail;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryDetailVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysDictionaryDetailService extends IService<SysDictionaryDetail> {

    IPage<SysDictionaryDetailVo> getSysDictionaryDetailList(SysDictionaryDetailQuery dictionaryDetailQuery);

    void createSysDictionaryDetail(SysDictionaryDetailDto dictionaryDetailDto);

    void updateSysDictionaryDetail(SysDictionaryDetailDto dictionaryDetailDto);

    void deleteSysDictionaryDetail(Long id);

    IPage<SysDictionaryDetailVo> getByDictionaryType(String sysDictionaryType, BasePage basePage);

    List<SysDictionaryDetailVo> getByDictionaryType(String type);
}
