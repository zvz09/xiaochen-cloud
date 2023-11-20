package com.zvz09.xiaochen.message.api;


import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.message.api.constant.FeignPath;
import com.zvz09.xiaochen.message.api.domain.dto.EventRemindDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 事件提醒 服务类
 *
 * @author zvz09
 * @date 2023-11-01 16:53:46
 */

@FeignClient(contextId = "RemoteEventRemindService", path = FeignPath.REMIND, value = ServiceNameConstants.MESSAGE_SERVICE)
public interface RemoteEventRemindService {

    @PostMapping("/notice")
    void notice(@RequestBody EventRemindDto eventRemindDto);
}
