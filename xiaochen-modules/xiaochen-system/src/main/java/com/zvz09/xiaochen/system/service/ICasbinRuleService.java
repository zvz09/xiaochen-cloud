package com.zvz09.xiaochen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.system.api.domain.dto.UpdateCasbinDto;
import com.zvz09.xiaochen.system.api.domain.entity.CasbinRule;
import com.zvz09.xiaochen.system.api.domain.vo.CasbinVo;

import java.util.List;

/**
 * @author zvz09
 */
public interface ICasbinRuleService extends IService<CasbinRule> {

    /**
     * 从旧角色拷贝权限
     *
     * @param newRoleCode
     * @param oldRoleCode
     */
    void copyRule(String newRoleCode, String oldRoleCode);

    void saveBatch(List<CasbinRule> casbinRules);

    /**
     * 删除角色权限
     *
     * @param roleCode
     */
    void removeRule(String roleCode);

    List<CasbinVo> listByRoleCode(String roleCode);

    void updateCasbin(UpdateCasbinDto updateCasbinDto);
}
