package com.zvz09.xiaochen.autocode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.autocode.domain.entity.SysGenCodeHistory;
import com.zvz09.xiaochen.common.core.page.BasePage;

/**
 * 历史记录 服务类
 *
 * @author zvz09
 * @date 2023-10-11 10:42:55
 */

public interface ISysGenCodeHistoryService extends IService<SysGenCodeHistory> {


    Page<SysGenCodeHistory> getGenCodeHistoryList(BasePage queryDto);
}
