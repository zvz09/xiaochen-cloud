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
     * @param newAuthorityCode
     * @param oldAuthorityCode
     */
    void copyRule(String newAuthorityCode, String oldAuthorityCode);

    void saveBatch(List<CasbinRule> casbinRules);

    /**
     * 删除角色权限
     * @param authorityCode
     */
    void removeRule(String authorityCode);

    List<CasbinVo> listByAuthorityCode(String authorityCode);

    void updateCasbin(UpdateCasbinDto updateCasbinDto);
}
