package com.zvz09.xiaochen.job.core;


import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.core.constant.FeignPath;
import com.zvz09.xiaochen.job.core.model.HandleCallbackParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author lizili-YF0033
 */
@FeignClient(contextId = "ExecutorBizAdmin", path = FeignPath.JOB_CLIENT, value = ServiceNameConstants.JOB_ADMIN_SERVICE)
public interface ExecutorBizAdmin {

    /**
     * beat
     * @return
     */
    @GetMapping("/callback")
    public ApiResult<String> callback(@RequestBody HandleCallbackParam  handleCallbackParam);



}
