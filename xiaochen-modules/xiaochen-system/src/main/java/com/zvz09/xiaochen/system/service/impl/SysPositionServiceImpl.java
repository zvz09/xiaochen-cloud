package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.domain.dto.position.PositionDto;
import com.zvz09.xiaochen.system.api.domain.dto.position.QueryDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPosition;
import com.zvz09.xiaochen.system.mapper.SysPositionMapper;
import com.zvz09.xiaochen.system.service.ISysPositionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位管理 服务实现类
 *
 * @author zvz09
 * @date 2023-10-10 13:50:31
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {

    @Override
    public void createPosition(PositionDto positionDto) {
        if (this.count(new LambdaQueryWrapper<SysPosition>()
                .eq(SysPosition::getPositionCode, positionDto.getPositionCode()).or()
                .eq(SysPosition::getPositionName, positionDto.getPositionName()).or()
        ) > 0) {
            String msg = " 岗位编码/岗位名称已存在";
            throw new BusinessException(msg);
        }

        this.save(positionDto.convertedToPo());
    }

    @Override
    public void deletePositionByIds(List<Long> ids) {
        if (ids == null || !ids.isEmpty()) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysPosition>().in(SysPosition::getId, ids)
                .set(SysPosition::getDeleted, true));
    }

    @Override
    public void deletePosition(Long id) {
        if (id == null) {
            return;
        }
        this.update(new LambdaUpdateWrapper<SysPosition>().eq(SysPosition::getId, id)
                .set(SysPosition::getDeleted, true));
    }

    @Override
    public void updatePosition(PositionDto positionDto) {
        if (this.count(new LambdaQueryWrapper<SysPosition>()
                .eq(SysPosition::getPositionCode, positionDto.getPositionCode())
                .eq(SysPosition::getPositionName, positionDto.getPositionName())
                .ne(SysPosition::getId, positionDto.getId())
        ) > 0) {
            String msg = " ${soleColumn.genConfig.description}/ ' ${soleColumn.genConfig.description}/ ''已存在";
            throw new BusinessException(msg);
        }
        this.updateById(positionDto.convertedToPo());
    }

    @Override
    public Page<SysPosition> getPositionList(QueryDto queryDto) {
        return this.page(new Page<>(queryDto.getPage(), queryDto.getPageSize()),
                new LambdaQueryWrapper<SysPosition>()
                        .like(StringUtils.isNotBlank(queryDto.getPositionCode()), SysPosition::getPositionCode, queryDto.getPositionCode())
                        .like(StringUtils.isNotBlank(queryDto.getPositionName()), SysPosition::getPositionName, queryDto.getPositionName())
        );
    }
}
