package com.zvz09.xiaochen.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.system.api.domain.dto.UpdateCasbinDto;
import com.zvz09.xiaochen.system.api.domain.entity.CasbinRule;
import com.zvz09.xiaochen.system.api.domain.vo.CasbinVo;
import com.zvz09.xiaochen.system.mapper.CasbinRuleMapper;
import com.zvz09.xiaochen.system.service.ICasbinRuleService;
import lombok.RequiredArgsConstructor;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * default description 服务实现类
 *
 * @author Default Author
 * @date 2023-11-14 10:48:39
 */
@Service
@RequiredArgsConstructor
public class CasbinRuleServiceImpl extends ServiceImpl<CasbinRuleMapper, CasbinRule> implements ICasbinRuleService {
    private final Enforcer enforcer;


    @Override
    @Transactional
    public void copyRule(String newAuthorityCode, String oldAuthorityCode) {
        List<List<String>> list = enforcer.getFilteredPolicy(0, oldAuthorityCode);
        List<CasbinRule> casbinRuleList = new ArrayList<>();
        list.forEach(l -> {
            l.set(0, newAuthorityCode);
            casbinRuleList.add(conversion(l));
        });
        this.removeRule(newAuthorityCode);
        this.saveBatch(casbinRuleList);
    }

    @Override
    public void saveBatch(List<CasbinRule> casbinRules) {
        super.saveBatch(casbinRules);
        List<List<String>> list = new ArrayList<>();
        casbinRules.forEach(casbinRule -> {
            List<String> strings = new ArrayList<>();
            strings.add(casbinRule.getV0());
            strings.add(casbinRule.getV1());
            strings.add(casbinRule.getV2());
            list.add(strings);
        });
        enforcer.addPolicies(list);
    }

    @Override
    public void removeRule(String authorityCode) {
        this.remove(new LambdaQueryWrapper<CasbinRule>().eq(CasbinRule::getV0,authorityCode));
        enforcer.removeFilteredPolicy(0, authorityCode);
    }

    @Override
    public List<CasbinVo> listByAuthorityCode(String authorityCode) {
        List<CasbinRule> casbinRuleList = this.list(new LambdaQueryWrapper<CasbinRule>().eq(CasbinRule::getV0,authorityCode));
        List<CasbinVo> casbinVos = new ArrayList<>();
        if (casbinRuleList != null && !casbinRuleList.isEmpty()) {
            casbinRuleList.forEach(e -> {
                CasbinVo casbinVo = new CasbinVo();
                casbinVo.setAuthorityCode(e.getV0());
                casbinVo.setPath(e.getV1());
                casbinVo.setMethod(e.getV2());
                casbinVos.add(casbinVo);
            });
        }
        return casbinVos;
    }

    @Override
    public void updateCasbin(UpdateCasbinDto updateCasbinDto) {
        this.removeRule(updateCasbinDto.getAuthorityCode());
        List<CasbinRule> casbinRuleList = new ArrayList<>();
        updateCasbinDto.getCasbinInfos().forEach(casbinInfo -> {
            CasbinRule casbinRule = new CasbinRule();
            casbinRule.setPtype("p");
            casbinRule.setV0(updateCasbinDto.getAuthorityCode());
            casbinRule.setV1(casbinInfo.getPath());
            casbinRule.setV2(casbinInfo.getMethod());
            casbinRuleList.add(casbinRule);
        });
        this.saveBatch(casbinRuleList);
    }

    private CasbinRule conversion(List<String> rule) {
        CasbinRule line = new CasbinRule();
        line.setPtype("p");
        if (rule.size() > 0) {
            line.setV0(rule.get(0));
        }
        if (rule.size() > 1) {
            line.setV1(rule.get(1));
        }
        if (rule.size() > 2) {
            line.setV2(rule.get(2));
        }
        if (rule.size() > 3) {
            line.setV3(rule.get(3));
        }
        if (rule.size() > 4) {
            line.setV4(rule.get(4));
        }
        if (rule.size() > 5) {
            line.setV5(rule.get(5));
        }

        return line;
    }
}
