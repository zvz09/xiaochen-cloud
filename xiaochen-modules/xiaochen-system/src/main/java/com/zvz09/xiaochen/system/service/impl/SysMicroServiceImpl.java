package com.zvz09.xiaochen.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.system.api.domain.entity.SysMicro;
import com.zvz09.xiaochen.system.api.domain.vo.SysMenuVo;
import com.zvz09.xiaochen.system.mapper.SysMicroMapper;
import com.zvz09.xiaochen.system.service.ISysMicroService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2024-01-29
 */
@Service
@RequiredArgsConstructor
public class SysMicroServiceImpl extends ServiceImpl<SysMicroMapper, SysMicro> implements ISysMicroService {


    @Override
    public IPage<SysMicro> page(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()));
    }

    @Override
    public List<SysMicro> listTree() {
        List<SysMicro> sysMicros = this.list();
        sysMicros.sort(Comparator.comparing(SysMicro::getSort));
        return sysMicros;
    }
}
