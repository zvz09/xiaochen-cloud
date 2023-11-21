package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiDto;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import com.zvz09.xiaochen.system.api.domain.vo.SysApiVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
public interface ISysApiService extends IService<SysApi> {

    void createApi(SysApiDto sysApiDto);

    void deleteApi(Long sysApiId);

    IPage<SysApiVo> getApiPage(SysApiQuery sysApiQuery);

    void updateApi(SysApiDto sysApiDto);

    void deleteApisByIds(List<Long> ids);

    List<SysApiVo> listTree();
    List<SysApiVo> listTree(List<Long> ids);
}
