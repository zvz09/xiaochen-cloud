package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.core.util.TreeBuilder;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.system.api.constant.PermCodeType;
import com.zvz09.xiaochen.system.api.domain.dto.menu.SysMenuDto;
import com.zvz09.xiaochen.system.api.domain.dto.perm.SysPermCodeDto;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCode;
import com.zvz09.xiaochen.system.api.domain.entity.SysPermCodeApi;
import com.zvz09.xiaochen.system.api.domain.vo.SysApiVo;
import com.zvz09.xiaochen.system.api.domain.vo.SysPermCodeVo;
import com.zvz09.xiaochen.system.converter.SysPermCodeTreeConverter;
import com.zvz09.xiaochen.system.mapper.SysPermCodeMapper;
import com.zvz09.xiaochen.system.service.ISysApiService;
import com.zvz09.xiaochen.system.service.ISysPermCodeApiService;
import com.zvz09.xiaochen.system.service.ISysPermCodeService;
import com.zvz09.xiaochen.system.service.ISysRolePermCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.zvz09.xiaochen.common.core.constant.Constants.SUPER_ADMIN;


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
    private final ISysRolePermCodeService sysRolePermCodeService;
    private final ISysApiService sysApiService;

    @Override
    @Transactional
    public void createAndSave(Long menuId, SysMenuDto sysMenuDto) {
        SysPermCode parent = this.getByMenuId(sysMenuDto.getParentId());
        SysPermCode sysPermCode =
                SysPermCode.builder().menuId(menuId)
                        .parentId(parent == null ? 0L : parent.getId())
                        .permCode(sysMenuDto.getName())
                        .permCodeType(PermCodeType.MENU.getType())
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
        if (parent == null) {
            throw new BusinessException("请选择菜单");
        }
        Long menuId = parent.getMenuId();

        SysPermCode dbData = null;
        if (sysPermCodeDto.getId() != null) {
            dbData = this.getById(sysPermCodeDto.getParentId());
        }

        SysPermCode sysPermCode =
                SysPermCode.builder().menuId(menuId)
                        .parentId(parent.getId())
                        .permCode(sysPermCodeDto.getPermCode())
                        .permCodeType(sysPermCodeDto.getPermCodeType())
                        .showName(sysPermCodeDto.getShowName())
                        .showOrder(sysPermCodeDto.getShowOrder())
                        .build();

        if (dbData != null) {
            sysPermCode.setId(dbData.getId());
            this.updateById(sysPermCode);
        } else {
            this.save(sysPermCode);
        }
    }

    @Override
    public IPage<SysPermCodeVo> listTree(BasePage basePage) {
        if (basePage == null || StringUtils.isBlank(basePage.getKeyword())) {
            List<SysPermCode> sysPermCodeList = this.list();
            if(sysPermCodeList == null || sysPermCodeList.isEmpty()){
                return new Page<>(1, 0);
            }
            List<SysPermCodeVo> voList = new TreeBuilder<SysPermCode, SysPermCodeVo>(t -> t.getParentId() == 0L)
                    .buildTree(sysPermCodeList, new SysPermCodeTreeConverter(),Comparator.comparing(SysPermCodeVo::getShowOrder));
            Page<SysPermCodeVo> page = new Page<>(1, voList.size());
            page.setRecords(voList);
            page.setTotal(voList.size());
            return page;
        }

        return null;
    }

    @Override
    public List<SysPermCodeVo> listTree(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return null;
        }
        List<SysPermCode> sysPermCodeList = this.list(new LambdaQueryWrapper<SysPermCode>().in(SysPermCode::getId,ids));
        return new TreeBuilder<SysPermCode, SysPermCodeVo>(t -> t.getParentId() == 0L)
                .buildTree(sysPermCodeList, new SysPermCodeTreeConverter(),Comparator.comparing(SysPermCodeVo::getShowOrder));
    }

    @Override
    public Map<String, List<String>> listPermCodes() {
        List<SysPermCode> sysPermCodeList;
        Map<String, List<String>> result = new HashMap<>();
        if (SUPER_ADMIN.equals(SecurityContextHolder.getRoleCode())) {
            sysPermCodeList = this.list();
        } else {
            List<Long> permCodeIds = sysRolePermCodeService.getPermCodeIdByRoleId(SecurityContextHolder.getRoleId());

            if (permCodeIds.isEmpty()) {
                return result;
            }
            sysPermCodeList = list(new LambdaQueryWrapper<SysPermCode>()
                    .in(SysPermCode::getId, permCodeIds)
                    .orderByAsc(SysPermCode::getPermCodeType));
        }

        List<SysPermCode> parentNodes = sysPermCodeList.stream()
                .filter(t -> Objects.equals(t.getPermCodeType(), PermCodeType.MENU.getType()))
                .toList();

        List<SysPermCode> childNodes = sysPermCodeList.stream()
                .filter(t -> !Objects.equals(t.getPermCodeType(), PermCodeType.MENU.getType()))
                .toList();

        childNodes.forEach(sysPermCode -> parentNodes.stream()
                .filter(t -> Objects.equals(t.getId(), sysPermCode.getParentId()))
                .findFirst().ifPresent(parent -> result.computeIfAbsent(parent.getPermCode(), k -> new ArrayList<>())
                        .add(sysPermCode.getPermCode())));

        return result;
    }

    @Override
    public SysPermCodeVo detail(Long id) {
        SysPermCode sysPermCode = this.getById(id);
        if (sysPermCode == null) {
            return null;
        }
        List<Long> apiIds = sysPermCodeApiService.getApiIdByPermCodeId(id);
        List<SysApiVo> sysApiVos = sysApiService.listTree(apiIds);
        return new SysPermCodeVo(sysPermCode, apiIds, sysApiVos);
    }

    @Override
    public void bindApis(Long id, List<Long> apiIds) {
        SysPermCode sysPermCode = this.getById(id);
        if (sysPermCode == null) {
            return;
        }
        sysPermCodeApiService.deleteByPermCodeId(id);
        if (apiIds != null && !apiIds.isEmpty()) {
            List<SysPermCodeApi> sysPermCodeApiList = new ArrayList<>();
            apiIds.forEach(s -> sysPermCodeApiList.add(new SysPermCodeApi(sysPermCode.getId(), s)));
            sysPermCodeApiService.saveBatch(sysPermCodeApiList);
        }
    }

    @Override
    public void updatePermCode(Long menuId, SysMenuDto sysMenuDto) {
        SysPermCode sysPermCode =
                this.getOne(new LambdaQueryWrapper<SysPermCode>()
                        .eq(SysPermCode::getMenuId,menuId)
                        .eq(SysPermCode::getPermCodeType,PermCodeType.MENU.getType()));
        if(sysPermCode == null){
            return;
        }
        sysPermCode.setShowOrder(Math.toIntExact(sysMenuDto.getSort()));
        sysPermCode.setShowName(sysMenuDto.getTitle());
        sysPermCode.setPermCode(sysMenuDto.getName());
        this.updateById(sysPermCode);
    }


}
