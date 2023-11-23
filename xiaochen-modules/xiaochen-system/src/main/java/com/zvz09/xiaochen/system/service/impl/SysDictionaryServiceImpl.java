package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionary;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryVo;
import com.zvz09.xiaochen.system.mapper.SysDictionaryMapper;
import com.zvz09.xiaochen.system.service.ISysDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements ISysDictionaryService {

    @Override
    public IPage<SysDictionaryVo> getSysDictionaryList(SysDictionaryQuery sysDictionaryQuery) {

        IPage<SysDictionary> dictionaryPage = this.page(new Page<>(sysDictionaryQuery.getPageNum(), sysDictionaryQuery.getPageSize()),
                new LambdaQueryWrapper<SysDictionary>()
                        .like(StringUtils.isNotBlank(sysDictionaryQuery.getName()), SysDictionary::getName, sysDictionaryQuery.getName())
                        .like(StringUtils.isNotBlank(sysDictionaryQuery.getEncode()), SysDictionary::getEncode, sysDictionaryQuery.getEncode())
                        .eq(sysDictionaryQuery.getStatus() != null, SysDictionary::getStatus, sysDictionaryQuery.getStatus()));


        return dictionaryPage.convert(SysDictionaryVo::new);
    }

    @Override
    public void createSysDictionary(SysDictionaryDto sysDictionaryDto) {
        if (this.count(new LambdaQueryWrapper<SysDictionary>()
                .eq(SysDictionary::getEncode, sysDictionaryDto.getEncode())) > 0) {
            throw new BusinessException("存在相同的英文编码，不允许创建");
        }
        this.save(sysDictionaryDto.convertedToPo());
    }

    @Override
    public void updateSysDictionary(SysDictionaryDto sysDictionaryDto) {
        SysDictionary sysDictionary = this.getById(sysDictionaryDto.getId());
        if (sysDictionary == null) {
            return;
        }
        this.updateById(sysDictionaryDto.convertedToPo());
    }

    @Override
    public void deleteSysDictionary(Long id) {
        SysDictionary sysDictionary = this.getById(id);
        if (sysDictionary == null) {
            return;
        }

        this.update(new LambdaUpdateWrapper<SysDictionary>().eq(SysDictionary::getId, id)
                .set(SysDictionary::getDeleted, true));
    }

    @Override
    public void changeStatus(Long id) {
        SysDictionary sysDictionary = this.getById(id);
        if (sysDictionary == null) {
            return;
        }
        sysDictionary.setStatus(!sysDictionary.getStatus());
        this.updateById(sysDictionary);
    }
}
