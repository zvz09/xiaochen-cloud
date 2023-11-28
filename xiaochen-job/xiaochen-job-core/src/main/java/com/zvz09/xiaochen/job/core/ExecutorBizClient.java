package com.zvz09.xiaochen.job.core;


import com.zvz09.xiaochen.common.core.constant.ServiceNameConstants;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.job.core.constant.FeignPath;
import com.zvz09.xiaochen.job.core.model.IdleBeatParam;
import com.zvz09.xiaochen.job.core.model.KillParam;
import com.zvz09.xiaochen.job.core.model.TriggerParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author xuxueli
 * @date 17/3/1
 */
public interface ExecutorBizClient {

    /**
     * beat
     * @return
     */
    @GetMapping("/beat")
    public ApiResult<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     * @return
     */
    @PostMapping("/idleBeat")
    public ApiResult<String> idleBeat(@RequestBody IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     * @return
     */
    @PostMapping("/run")
    public ApiResult<String> run(@RequestBody TriggerParam triggerParam);

    /**
     * kill
     * @param killParam
     * @return
     */
    @DeleteMapping("/kill")
    public ApiResult<String> kill(@RequestBody KillParam killParam);


}
