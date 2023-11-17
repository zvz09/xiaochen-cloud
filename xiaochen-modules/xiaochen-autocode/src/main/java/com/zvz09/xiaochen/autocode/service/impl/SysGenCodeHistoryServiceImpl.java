package com.zvz09.xiaochen.autocode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.autocode.domain.entity.SysGenCodeHistory;
import com.zvz09.xiaochen.autocode.mapper.SysGenCodeHistoryMapper;
import com.zvz09.xiaochen.autocode.service.ISysGenCodeHistoryService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import org.springframework.stereotype.Service;

/**
 * 历史记录 服务实现类
 *
 * @author zvz09
 * @date 2023-10-11 10:42:55
 */
@Service
public class SysGenCodeHistoryServiceImpl extends ServiceImpl<SysGenCodeHistoryMapper, SysGenCodeHistory> implements ISysGenCodeHistoryService {

    @Override
    public Page<SysGenCodeHistory> getGenCodeHistoryList(BasePage queryDto) {
        return this.page(new Page<>(queryDto.getPageNum(), queryDto.getPageSize()),
                new LambdaQueryWrapper<SysGenCodeHistory>().orderByDesc(SysGenCodeHistory::getCreatedAt)
        );
    }
}
