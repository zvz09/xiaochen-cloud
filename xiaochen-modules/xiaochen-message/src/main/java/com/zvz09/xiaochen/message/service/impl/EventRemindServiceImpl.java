package com.zvz09.xiaochen.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import com.zvz09.xiaochen.message.api.RemoteEventRemindService;
import com.zvz09.xiaochen.message.api.constant.ActionEnums;
import com.zvz09.xiaochen.message.api.constant.FeignPath;
import com.zvz09.xiaochen.message.api.constant.NoticeSourceType;
import com.zvz09.xiaochen.message.api.domain.dto.EventRemindDto;
import com.zvz09.xiaochen.message.api.domain.entity.EventRemind;
import com.zvz09.xiaochen.message.api.domain.vo.EventRemindVo;
import com.zvz09.xiaochen.message.mapper.EventRemindMapper;
import com.zvz09.xiaochen.message.service.IEventRemindService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件提醒 服务实现类
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */
@Slf4j
@Service
@RestController
@Tag(name = "feign-事件提醒")
@RequestMapping(FeignPath.REMIND)
@RequiredArgsConstructor
public class EventRemindServiceImpl extends ServiceImpl<EventRemindMapper, EventRemind> implements IEventRemindService, RemoteEventRemindService {

    @Override
    public void notice(String recipientId, ActionEnums actionEnums, NoticeSourceType noticeSourceType, String message) {
        EventRemind eventRemind = EventRemind.builder()
                .recipientId(Long.parseLong(recipientId))
                .senderId(0L)
                .action(actionEnums.getAction())
                .sourceType(noticeSourceType.getType())
                .sourceContent(message)
                .remindTime(LocalDateTime.now())
                .build();
        this.save(eventRemind);
    }

    @Override
    public IPage<EventRemindVo> getByType(String type, Boolean state, BasePage basePage) {
        IPage<EventRemind> page = this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize())
                , new LambdaQueryWrapper<EventRemind>().eq(StringUtils.isNotBlank(type), EventRemind::getSourceType, type)
                        .eq(state != null, EventRemind::getState, state)
                        .eq(EventRemind::getRecipientId, SecurityContextHolder.getUserId())
                        .orderByDesc(EventRemind::getRemindTime, EventRemind::getState));
        return page.convert(EventRemindVo::new);
    }

    @Override
    public Map<String, Long> getAllUnRead() {
        QueryWrapper<EventRemind> wrapper = new QueryWrapper<EventRemind>()
                .select("source_type,count(0) as remindCount")
                .eq("state", "0")
                .eq("recipient_id", SecurityContextHolder.getUserId())
                .groupBy("source_type");
        List<Map<String, Object>> listMaps = this.listMaps(wrapper);
        Map<String, Long> map = new HashMap<>();
        Long total = 0L;
        for (Map m : listMaps) {
            map.put(m.get("source_type").toString(), Long.parseLong(m.get("remindCount").toString()));
            total += Long.parseLong(m.get("remindCount").toString());
        }
        map.put("total", total);
        return map;
    }

    @Override
    public void confirm(List<Long> ids) {
        this.update(new LambdaUpdateWrapper<EventRemind>().set(EventRemind::getState, true)
                .eq(EventRemind::getRecipientId, SecurityContextHolder.getUserId())
                .in(EventRemind::getId, ids));
    }

    @Override
    public void notice(EventRemindDto eventRemindDto) {
        this.notice(eventRemindDto.getRecipientId(), eventRemindDto.getAction(),
                eventRemindDto.getSourceType(), eventRemindDto.getSourceContent());
    }
}
