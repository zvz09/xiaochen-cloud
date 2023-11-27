package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailDto;
import com.zvz09.xiaochen.system.api.domain.dto.dictionary.SysDictionaryDetailQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionary;
import com.zvz09.xiaochen.system.api.domain.entity.SysDictionaryDetail;
import com.zvz09.xiaochen.system.api.domain.vo.SysDictionaryDetailVo;
import com.zvz09.xiaochen.system.mapper.SysDictionaryDetailMapper;
import com.zvz09.xiaochen.system.service.ISysDictionaryDetailService;
import com.zvz09.xiaochen.system.service.ISysDictionaryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Service
@RequiredArgsConstructor
public class SysDictionaryDetailServiceImpl extends ServiceImpl<SysDictionaryDetailMapper, SysDictionaryDetail> implements ISysDictionaryDetailService {

    private final ISysDictionaryService sysDictionaryService;

    @Override
    public IPage<SysDictionaryDetailVo> getSysDictionaryDetailList(SysDictionaryDetailQuery dictionaryDetailQuery) {

        IPage<SysDictionaryDetail> dictionaryPage = this.page(new Page<>(dictionaryDetailQuery.getPageNum(), dictionaryDetailQuery.getPageSize()),
                new LambdaQueryWrapper<SysDictionaryDetail>().eq(SysDictionaryDetail::getSysDictionaryId, dictionaryDetailQuery.getSysDictionaryId())
                        .eq(StringUtils.isNotBlank(dictionaryDetailQuery.getLabel()), SysDictionaryDetail::getLabel, dictionaryDetailQuery.getLabel())
                        .eq(dictionaryDetailQuery.getValue() != null, SysDictionaryDetail::getValue, dictionaryDetailQuery.getValue())
                        .eq(dictionaryDetailQuery.getStatus() != null, SysDictionaryDetail::getStatus, dictionaryDetailQuery.getStatus()));

        return dictionaryPage.convert(SysDictionaryDetailVo::new);
    }

    @Override
    public void createSysDictionaryDetail(SysDictionaryDetailDto dictionaryDetailDto) {
        SysDictionary sysDictionary = this.sysDictionaryService.getById(dictionaryDetailDto.getSysDictionaryId());
        if (sysDictionary == null) {
            throw new BusinessException("字典不存在");
        }

        if (this.count(new LambdaQueryWrapper<SysDictionaryDetail>()
                .eq(SysDictionaryDetail::getSysDictionaryId, dictionaryDetailDto.getSysDictionaryId())
                .and(i -> i.eq(SysDictionaryDetail::getLabel, dictionaryDetailDto.getLabel())
                        .or()
                        .eq(SysDictionaryDetail::getValue, dictionaryDetailDto.getValue()))) > 0) {
            throw new BusinessException("字典展示值/字典值已存在");
        }

        this.save(dictionaryDetailDto.convertedToPo());
    }

    @Override
    public void updateSysDictionaryDetail(SysDictionaryDetailDto dictionaryDetailDto) {
        SysDictionary sysDictionary = this.sysDictionaryService.getById(dictionaryDetailDto.getSysDictionaryId());
        if (sysDictionary == null) {
            throw new BusinessException("字典不存在");
        }
        SysDictionaryDetail sysDictionaryDetail = this.getById(dictionaryDetailDto.getId());
        if (sysDictionaryDetail == null) {
            throw new BusinessException("字典项不存在");
        }
        if (this.count(new LambdaQueryWrapper<SysDictionaryDetail>()
                .eq(SysDictionaryDetail::getSysDictionaryId, dictionaryDetailDto.getSysDictionaryId())
                .ne(SysDictionaryDetail::getId, dictionaryDetailDto.getId())
                .and(i -> i.eq(SysDictionaryDetail::getLabel, dictionaryDetailDto.getLabel())
                        .or()
                        .eq(SysDictionaryDetail::getValue, dictionaryDetailDto.getValue()))) > 0) {
            throw new BusinessException("字典展示值/字典值已存在");
        }

        this.updateById(dictionaryDetailDto.convertedToPo());
    }

    @Override
    public void deleteSysDictionaryDetail(Long id) {
        SysDictionaryDetail sysDictionaryDetail = this.getById(id);
        if (sysDictionaryDetail == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysDictionaryDetail>().eq(SysDictionaryDetail::getId, id)
                .set(SysDictionaryDetail::getDeleted, true));
    }

    @Override
    public IPage<SysDictionaryDetailVo> getByDictionaryEncode(String encode, BasePage basePage) {
        SysDictionary sysDictionary =
                this.sysDictionaryService.getOne(new LambdaQueryWrapper<SysDictionary>()
                        .eq(SysDictionary::getEncode, encode));
        if (sysDictionary == null) {
            return new Page<>(basePage.getPageNum(), basePage.getPageSize());
        }
        SysDictionaryDetailQuery dictionaryDetailQuery = new SysDictionaryDetailQuery();
        dictionaryDetailQuery.setSysDictionaryId(sysDictionary.getId());
        dictionaryDetailQuery.setLabel(basePage.getKeyword());
        dictionaryDetailQuery.setPageNum(basePage.getPageNum());
        dictionaryDetailQuery.setPageSize(basePage.getPageSize());
        return this.getSysDictionaryDetailList(dictionaryDetailQuery);
    }

    @Override
    public List<SysDictionaryDetailVo> getByDictionaryEncode(String encode) {
        List<SysDictionaryDetailVo> voList = new ArrayList<>();
        SysDictionary sysDictionary =
                this.sysDictionaryService.getOne(new LambdaQueryWrapper<SysDictionary>()
                        .eq(SysDictionary::getEncode, encode).eq(SysDictionary::getStatus,true));
        if (sysDictionary == null) {
            return voList;
        }
        List<SysDictionaryDetail> sysDictionaryDetails = this.list(new LambdaQueryWrapper<SysDictionaryDetail>()
                .eq(SysDictionaryDetail::getSysDictionaryId, sysDictionary.getId())
                .eq(SysDictionaryDetail::getStatus,true));

        sysDictionaryDetails.forEach(sysDictionaryDetail -> {
            voList.add(new SysDictionaryDetailVo(sysDictionaryDetail));
        });
        return voList;
    }

    @Override
    public void changeStatus(Long id) {
        SysDictionaryDetail sysDictionaryDetail = this.getById(id);
        if (sysDictionaryDetail == null) {
            return;
        }
        sysDictionaryDetail.setStatus(!sysDictionaryDetail.getStatus());
        this.updateById(sysDictionaryDetail);
    }
}
