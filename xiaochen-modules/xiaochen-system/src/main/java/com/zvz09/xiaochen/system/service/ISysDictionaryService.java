package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionary;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysDictionaryService extends IService<SysDictionary> {

    IPage<SysDictionaryVo> getSysDictionaryList(SysDictionaryQuery sysDictionaryQuery);

    void createSysDictionary(SysDictionaryDto sysDictionaryDto);

    void updateSysDictionary(SysDictionaryDto sysDictionaryDto);

    void deleteSysDictionary(Long id);

    void changeStatus(Long id);
}
