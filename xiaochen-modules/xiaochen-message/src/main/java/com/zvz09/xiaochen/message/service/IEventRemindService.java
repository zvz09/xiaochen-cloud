package com.zvz09.xiaochen.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.message.api.constant.ActionEnums;
import com.zvz09.xiaochen.message.api.constant.NoticeSourceType;
import com.zvz09.xiaochen.message.api.domain.entity.EventRemind;
import com.zvz09.xiaochen.message.api.domain.vo.EventRemindVo;

import java.util.List;
import java.util.Map;

/**
 * 事件提醒 服务类
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */

public interface IEventRemindService extends IService<EventRemind> {

    void notice(String recipientId, ActionEnums actionEnums, NoticeSourceType noticeSourceType, String message);

    IPage<EventRemindVo> getByType(String type, Boolean state, BasePage basePage);

    Map<String, Long> getAllUnRead();

    void confirm(List<Long> ids);
}
