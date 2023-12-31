package com.zvz09.xiaochen.flowable.utils;

import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.flowable.domain.dto.ProcessQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.flowable.common.engine.api.query.Query;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.Map;

/**
 * 流程工具类
 *
 * @author konbai
 * @since 2022/12/11 03:35
 */
@Slf4j
public class ProcessUtils {

    public static void buildProcessSearch(Query<?, ?> query, ProcessQuery process) {
        if (query instanceof ProcessDefinitionQuery) {
            buildProcessDefinitionSearch((ProcessDefinitionQuery) query, process);
        } else if (query instanceof TaskQuery) {
            buildTaskSearch((TaskQuery) query, process);
        } else if (query instanceof HistoricTaskInstanceQuery) {
            buildHistoricTaskInstanceSearch((HistoricTaskInstanceQuery) query, process);
        } else if (query instanceof HistoricProcessInstanceQuery) {
            buildHistoricProcessInstanceSearch((HistoricProcessInstanceQuery) query, process);
        }
    }

    /**
     * 构建流程定义搜索
     */
    public static void buildProcessDefinitionSearch(ProcessDefinitionQuery query, ProcessQuery process) {
        // 流程标识
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
        }
        // 流程名称
        if (StringUtils.isNotBlank(process.getProcessName())) {
            query.processDefinitionNameLike("%" + process.getProcessName() + "%");
        }
        // 流程分类
        if (StringUtils.isNotBlank(process.getCategory())) {
            query.processDefinitionCategory(process.getCategory());
        }
        // 流程状态
        if (StringUtils.isNotBlank(process.getState())) {
            if (SuspensionState.ACTIVE.toString().equals(process.getState())) {
                query.active();
            } else if (SuspensionState.SUSPENDED.toString().equals(process.getState())) {
                query.suspended();
            }
        }
    }

    /**
     * 构建任务搜索
     */
    public static void buildTaskSearch(TaskQuery query, ProcessQuery process) {
        try {
            Map<String, Object> params = process.getParams();
            if (StringUtils.isNotBlank(process.getProcessKey())) {
                query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
            }
            if (StringUtils.isNotBlank(process.getProcessName())) {
                query.processDefinitionNameLike("%" + process.getProcessName() + "%");
            }
            if (params.get("beginTime") != null && params.get("endTime") != null) {
                query.taskCreatedAfter(DateUtils.parseDate((String) params.get("beginTime")));
                query.taskCreatedBefore(DateUtils.parseDate((String) params.get("endTime")));
            }
        } catch (Exception e) {
            log.error("构建任务搜索查询条件异常", e);
            throw new BusinessException("构建任务搜索查询条件异常");
        }
    }

    private static void buildHistoricTaskInstanceSearch(HistoricTaskInstanceQuery query, ProcessQuery process) {
        try {
            Map<String, Object> params = process.getParams();
            if (StringUtils.isNotBlank(process.getProcessKey())) {
                query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
            }
            if (StringUtils.isNotBlank(process.getProcessName())) {
                query.processDefinitionNameLike("%" + process.getProcessName() + "%");
            }
            if (params.get("beginTime") != null && params.get("endTime") != null) {
                query.taskCompletedAfter(DateUtils.parseDate((String) params.get("beginTime")));
                query.taskCompletedBefore(DateUtils.parseDate((String) params.get("endTime")));
            }
        } catch (Exception e) {
            log.error("构建历史任务实例搜索条件异常", e);
            throw new BusinessException("构建历史任务实例搜索条件异常");
        }
    }

    /**
     * 构建历史流程实例搜索
     */
    public static void buildHistoricProcessInstanceSearch(HistoricProcessInstanceQuery query, ProcessQuery process) {
        try {
            Map<String, Object> params = process.getParams();
            // 流程标识
            if (StringUtils.isNotBlank(process.getProcessKey())) {
                query.processDefinitionKey(process.getProcessKey());
            }
            // 流程名称
            if (StringUtils.isNotBlank(process.getProcessName())) {
                query.processDefinitionName(process.getProcessName());
            }
            // 流程名称
            if (StringUtils.isNotBlank(process.getCategory())) {
                query.processDefinitionCategory(process.getCategory());
            }
            if (params.get("beginTime") != null && params.get("endTime") != null) {
                query.startedAfter(DateUtils.parseDate((String) params.get("beginTime")));
                query.startedBefore(DateUtils.parseDate((String) params.get("endTime")));
            }
        } catch (Exception e) {
            log.error("构建历史流程实例搜索条件异常", e);
            throw new BusinessException("构建历史流程实例搜索条件异常");
        }
    }

}
