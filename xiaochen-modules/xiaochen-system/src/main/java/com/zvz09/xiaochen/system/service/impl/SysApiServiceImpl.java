package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.RemoteSysApiService;
import com.zvz09.xiaochen.system.api.constant.FeignPath;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiDto;
import com.zvz09.xiaochen.system.api.domain.dto.api.SysApiQuery;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import com.zvz09.xiaochen.system.api.domain.vo.SysApiVo;
import com.zvz09.xiaochen.system.mapper.SysApiMapper;
import com.zvz09.xiaochen.system.service.ISysApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-08-30
 */
@Slf4j
@Service
@RestController
@RequestMapping(FeignPath.API)
@Tag(name = "feign-api接口")
@RequiredArgsConstructor
public class SysApiServiceImpl extends ServiceImpl<SysApiMapper, SysApi> implements ISysApiService, RemoteSysApiService {

    @Override
    public void createApi(SysApiDto sysApiDto) {
        if (this.getOne(new LambdaQueryWrapper<SysApi>()
                .eq(SysApi::getPath, sysApiDto.getPath())
                .eq(SysApi::getMethod, sysApiDto.getMethod())) != null) {
            throw new BusinessException("该接口路径和方式存在");
        }
        this.save(sysApiDto.convertedToPo());
    }

    @Override
    public void deleteApi(Long sysApiId) {
        if (sysApiId == null) {
            return;
        }
        LambdaUpdateWrapper<SysApi> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysApi::getId, sysApiId).set(SysApi::getDeleted, true);
        this.baseMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public IPage<SysApiVo> getApiPage(SysApiQuery sysApiQuery) {
        LambdaQueryWrapper<SysApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(sysApiQuery.getPath()), SysApi::getPath, sysApiQuery.getKeyword());
        queryWrapper.like(StringUtils.isNotEmpty(sysApiQuery.getDescription()), SysApi::getDescription, sysApiQuery.getDescription());
        queryWrapper.like(StringUtils.isNotEmpty(sysApiQuery.getApiGroup()), SysApi::getApiGroup, sysApiQuery.getApiGroup());
        queryWrapper.eq(StringUtils.isNotEmpty(sysApiQuery.getMethod()), SysApi::getMethod, sysApiQuery.getMethod());
        queryWrapper.orderByAsc(SysApi::getId);

        IPage<SysApi> sysApiPage = this.page(new Page<>(sysApiQuery.getPageNum(), sysApiQuery.getPageSize()), queryWrapper);

        return sysApiPage.convert(SysApiVo::new);
    }

    @Override
    public void updateApi(SysApiDto sysApiDto) {
        if (this.getOne(new LambdaQueryWrapper<SysApi>()
                .eq(SysApi::getPath, sysApiDto.getPath())
                .eq(SysApi::getMethod, sysApiDto.getMethod())
                .ne(SysApi::getId, sysApiDto.getId())) != null) {
            throw new BusinessException("该接口路径和方式存在");
        }
        SysApi sysApi = this.getById(sysApiDto.getId());
        if (sysApi != null) {
            sysApi.setApiGroup(sysApiDto.getApiGroup());
            sysApi.setPath(sysApiDto.getPath());
            sysApi.setMethod(sysApiDto.getMethod());
            sysApi.setDescription(sysApiDto.getDescription());
            this.updateById(sysApi);
        } else {
            throw new BusinessException("该接口不存在");
        }
    }

    @Override
    public void deleteApisByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<SysApi> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(SysApi::getId, ids).set(SysApi::getDeleted, true);
        this.baseMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public List<SysApiVo> listTree() {
        List<SysApi> sysApis = this.list();
        return buildApiTree(sysApis);
    }

    @Override
    public List<SysApiVo> listTree(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysApi> sysApis = this.list(new LambdaQueryWrapper<SysApi>().in(SysApi::getId, ids));
        return buildApiTree(sysApis);
    }

    public List<SysApiVo> buildApiTree(List<SysApi> apis) {
        Map<String, List<SysApiVo>> apiObj = new HashMap<>();

        if (apis != null) {
            for (SysApi sysApi : apis) {
                String apiGroup = sysApi.getApiGroup();
                if (apiObj.containsKey(apiGroup)) {
                    apiObj.get(apiGroup).add(new SysApiVo(sysApi));
                } else {
                    apiObj.put(apiGroup, new ArrayList<>());
                    apiObj.get(apiGroup).add(new SysApiVo(sysApi));
                }
            }
        }
        List<SysApiVo> apiTree = new ArrayList<>();
        for (String key : apiObj.keySet()) {
            SysApiVo sysApiVo = new SysApiVo();
            sysApiVo.setApiGroup(key);
            sysApiVo.setDescription(key + "组");
            sysApiVo.setChildren(apiObj.get(key));
            apiTree.add(sysApiVo);
        }
        return apiTree;
    }

    @Override
    public List<SysApi> list(String serviceName) {
        List<SysApi> sysApis = this.list(new LambdaQueryWrapper<SysApi>().eq(SysApi::getServiceName, serviceName));
        return (sysApis != null && sysApis.size() > 0) ? sysApis : new ArrayList<>();
    }

    @Override
    public void saveBatch(List<SysApi> sysApis) {
        super.saveBatch(sysApis);
    }
}
