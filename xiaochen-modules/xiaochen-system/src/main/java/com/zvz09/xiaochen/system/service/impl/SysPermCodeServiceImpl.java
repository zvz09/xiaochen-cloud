package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.util.TreeBuilder;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.dto.perm.SysPermCodeDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCodeApi;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;
import com.zvz09.xiaochen.system.converter.SysPermCodeTreeConverter;
import com.zvz09.xiaochen.system.mapper.SysPermCodeMapper;
import com.zvz09.xiaochen.system.service.ISysPermCodeApiService;
import com.zvz09.xiaochen.system.service.ISysPermCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 系统权限字 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-11-20
 */
@Service
@RequiredArgsConstructor
public class SysPermCodeServiceImpl extends ServiceImpl<SysPermCodeMapper, SysPermCode> implements ISysPermCodeService {


    private final ISysPermCodeApiService sysPermCodeApiService;

    @Override
    @Transactional
    public void createAndSave(Long menuId, SysMenuDto sysMenuDto) {
        SysPermCode parent = this.getByMenuId(sysMenuDto.getParentId());
        SysPermCode sysPermCode =
                SysPermCode.builder().menuId(menuId)
                        .parentId(parent == null ? 0L : parent.getId())
                        .permCodeType(0)
                        .showName(sysMenuDto.getTitle())
                        .showOrder(0)
                        .build();
        this.save(sysPermCode);
    }

    @Override
    public SysPermCode getByMenuId(String menuId) {
        return this.getOne(new LambdaQueryWrapper<SysPermCode>().eq(SysPermCode::getMenuId, menuId).eq(SysPermCode::getPermCodeType, 0));
    }

    @Override
    @Transactional
    public void create(SysPermCodeDto sysPermCodeDto) {
        saveOrUpdatePermCode(sysPermCodeDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sysPermCodeApiService.deleteByPermCodeId(id);
        this.removeById(id);
    }

    @Override
    @Transactional
    public void updatePermCode(SysPermCodeDto sysPermCodeDto) {
        saveOrUpdatePermCode(sysPermCodeDto);
    }

    private void saveOrUpdatePermCode(SysPermCodeDto sysPermCodeDto) {
        SysPermCode parent = this.getById(sysPermCodeDto.getParentId());
        if (parent == null && sysPermCodeDto.getMenuId() == null) {
            throw new BusinessException("请选择菜单");
        }
        Long menuId = sysPermCodeDto.getMenuId();
        if (parent != null) {
            menuId = parent.getMenuId();
        }
        SysPermCode dbData = null;
        if (sysPermCodeDto.getId() != null) {
            dbData = this.getById(sysPermCodeDto.getParentId());
        }

        SysPermCode sysPermCode =
                SysPermCode.builder().menuId(menuId)
                        .parentId(parent == null ? 0L : parent.getId())
                        .permCodeType(sysPermCodeDto.getPermCodeType())
                        .showName(sysPermCodeDto.getShowName())
                        .showOrder(sysPermCodeDto.getShowOrder())
                        .build();

        if (dbData != null) {
            sysPermCode.setId(dbData.getId());
            sysPermCodeApiService.deleteByPermCodeId(dbData.getId());
        }

        this.updateById(sysPermCode);

        if (sysPermCodeDto.getApiIds() != null) {
            List<SysPermCodeApi> sysPermCodeApiList = new ArrayList<>();
            sysPermCodeDto.getApiIds().forEach(s -> {
                sysPermCodeApiList.add(new SysPermCodeApi(sysPermCode.getId(), s));
            });
            sysPermCodeApiService.saveBatch(sysPermCodeApiList);
        }
    }

    @Override
    public IPage<SysPermCodeVo> listTree(BasePage basePage) {
        if (StringUtils.isBlank(basePage.getKeyword())) {
            List<SysPermCode> sysPermCodeList = this.list();
            List<SysPermCodeVo> voList = new TreeBuilder<SysPermCode, SysPermCodeVo>(t -> t.getParentId() == 0L)
                    .buildTree(sysPermCodeList, new SysPermCodeTreeConverter());
            Page<SysPermCodeVo> page = new Page<>(basePage.getPageNum(), voList.size());
            page.setRecords(voList);
            return page;
        }

        return null;
    }

}
