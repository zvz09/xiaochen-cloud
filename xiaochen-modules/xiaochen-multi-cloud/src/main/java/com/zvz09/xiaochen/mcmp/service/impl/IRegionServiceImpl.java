package com.zvz09.xiaochen.mcmp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.mcmp.domain.entity.Region;
import com.zvz09.xiaochen.mcmp.mapper.RegionMapper;
import com.zvz09.xiaochen.mcmp.service.IRegionService;
import org.springframework.stereotype.Service;

/**
* @author lizili-YF0033
* @description 针对表【mcmp_region(地域表)】的数据库操作Service实现
* @createDate 2024-03-11 14:15:50
*/
@Service
public class IRegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {


    @Override
    public <P extends BasePage, V> IPage<V> page(P p) {
        return null;
    }
}




