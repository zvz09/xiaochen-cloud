package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.position.PositionDto;
import com.zvz09.xiaochen.system.api.domain.dto.position.QueryDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPosition;

import java.util.List;

/**
 * 岗位管理 服务类
 *
 * @author zvz09
 * @date 2023-10-10 13:50:31
 */

public interface ISysPositionService extends IService<SysPosition> {

    void createPosition(PositionDto positionDto);

    void deletePositionByIds(List<Long> ids);

    void deletePosition(Long id);

    void updatePosition(PositionDto positionDto);

    Page<SysPosition> getPositionList(QueryDto queryDto);
}
