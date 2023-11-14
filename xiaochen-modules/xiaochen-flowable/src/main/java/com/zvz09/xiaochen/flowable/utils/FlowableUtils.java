package com.zvz09.xiaochen.flowable.utils;

import cn.hutool.core.util.ObjectUtil;
import com.zvz09.xiaochen.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Gateway;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.UserTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class FlowableUtils {

    /**
     * 根据节点，获取入口连线
     *
     * @param source
     * @return
     */
    public static List<SequenceFlow> getElementIncomingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getIncomingFlows();
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getIncomingFlows();
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getIncomingFlows();
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getIncomingFlows();
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getIncomingFlows();
        }
        return sequenceFlows;
    }

    /**
     * 根据节点，获取出口连线
     *
     * @param source
     * @return
     */
    public static List<SequenceFlow> getElementOutgoingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getOutgoingFlows();
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getOutgoingFlows();
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getOutgoingFlows();
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getOutgoingFlows();
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getOutgoingFlows();
        }
        return sequenceFlows;
    }

    /**
     * 获取全部节点列表，包含子流程节点
     *
     * @param flowElements
     * @param allElements
     * @return
     */
    public static Collection<FlowElement> getAllElements(Collection<FlowElement> flowElements, Collection<FlowElement> allElements) {
        allElements = allElements == null ? new ArrayList<>() : allElements;

        for (FlowElement flowElement : flowElements) {
            allElements.add(flowElement);
            if (flowElement instanceof SubProcess) {
                // 继续深入子流程，进一步获取子流程
                allElements = FlowableUtils.getAllElements(((SubProcess) flowElement).getFlowElements(), allElements);
            }
        }
        return allElements;
    }

    /**
     * 迭代获取父级节点列表，向前找
     *
     * @param source          起始节点
     * @param hasSequenceFlow 已经经过的连线的ID，用于判断线路是否重复
     * @return
     */
    public static List<String> iteratorFindFinishes(FlowElement source, List<String> hasSequenceFlow) {
        hasSequenceFlow = hasSequenceFlow == null ? new ArrayList<>() : hasSequenceFlow;

        // 根据类型，获取入口连线
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);

        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                FlowElement finishedElement = sequenceFlow.getSourceFlowElement();
                // 类型为子流程，则添加子流程开始节点出口处相连的节点
                if (finishedElement instanceof SubProcess) {
                    FlowElement firstElement = (StartEvent) ((SubProcess) finishedElement).getFlowElements().toArray()[0];
                    // 获取子流程的连线
                    hasSequenceFlow.addAll(iteratorFindFinishes(firstElement, null));
                }
                // 继续迭代
                hasSequenceFlow = iteratorFindFinishes(finishedElement, hasSequenceFlow);
            }
        }
        return hasSequenceFlow;
    }

    /**
     * 根据正在运行的任务节点，迭代获取子级任务节点列表，向后找
     *
     * @param source                  起始节点
     * @param finishedSequenceFlowSet 已经完成的连线
     * @param finishedTaskSet         已经完成的任务节点
     * @param hasSequenceFlow         已经经过的连线的 ID，用于判断线路是否重复
     * @param rejectedList            未通过的元素
     * @return
     */
    public static List<String> iteratorFindRejects(FlowElement source, Set<String> finishedSequenceFlowSet, Set<String> finishedTaskSet
            , List<String> hasSequenceFlow, List<String> rejectedList) {
        hasSequenceFlow = hasSequenceFlow == null ? new ArrayList<>() : hasSequenceFlow;
        rejectedList = rejectedList == null ? new ArrayList<>() : rejectedList;

        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);

        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                FlowElement targetElement = sequenceFlow.getTargetFlowElement();
                // 添加未完成的节点
                if (finishedTaskSet.contains(targetElement.getId())) {
                    rejectedList.add(targetElement.getId());
                }
                // 添加未完成的连线
                if (finishedSequenceFlowSet.contains(sequenceFlow.getId())) {
                    rejectedList.add(sequenceFlow.getId());
                }
                // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
                if (targetElement instanceof SubProcess) {
                    FlowElement firstElement = (FlowElement) (((SubProcess) targetElement).getFlowElements().toArray()[0]);
                    List<String> childList = iteratorFindRejects(firstElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, null);
                    // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
                    if (childList != null && childList.size() > 0) {
                        rejectedList.addAll(childList);
                        continue;
                    }
                }
                // 继续迭代
                rejectedList = iteratorFindRejects(targetElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, rejectedList);
            }
        }
        return rejectedList;
    }

    /**
     * 深搜递归获取流程未通过的节点
     *
     * @param bpmnModel               流程模型
     * @param unfinishedTaskSet       未结束的任务节点
     * @param finishedSequenceFlowSet 已经完成的连线
     * @param finishedTaskSet         已完成的任务节点
     * @return
     */
    public static Set<String> dfsFindRejects(BpmnModel bpmnModel, Set<String> unfinishedTaskSet, Set<String> finishedSequenceFlowSet, Set<String> finishedTaskSet) {
        if (ObjectUtil.isNull(bpmnModel)) {
            throw new BusinessException("流程模型不存在");
        }
        Collection<FlowElement> allElements = getAllElements(bpmnModel.getMainProcess().getFlowElements(), null);
        Set<String> rejectedSet = new HashSet<>();
        for (FlowElement flowElement : allElements) {
            // 用户节点且未结束元素
            if (flowElement instanceof UserTask && unfinishedTaskSet.contains(flowElement.getId())) {
                List<String> hasSequenceFlow = iteratorFindFinishes(flowElement, null);
                List<String> rejects = iteratorFindRejects(flowElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, null);
                rejectedSet.addAll(rejects);
            }
        }
        return rejectedSet;
    }

    /**
     * 根据正在运行的任务节点，迭代获取子级任务节点列表，向后找
     *
     * @param source          起始节点
     * @param runTaskKeyList  正在运行的任务 Key，用于校验任务节点是否是正在运行的节点
     * @param hasSequenceFlow 已经经过的连线的 ID，用于判断线路是否重复
     * @param userTaskList    需要撤回的用户任务列表
     * @return
     */
    public static List<UserTask> iteratorFindChildUserTasks(FlowElement source, List<String> runTaskKeyList, Set<String> hasSequenceFlow, List<UserTask> userTaskList) {
        hasSequenceFlow = hasSequenceFlow == null ? new HashSet<>() : hasSequenceFlow;
        userTaskList = userTaskList == null ? new ArrayList<>() : userTaskList;

        // 如果该节点为开始节点，且存在上级子节点，则顺着上级子节点继续迭代
        if (source instanceof StartEvent && source.getSubProcess() != null) {
            userTaskList = iteratorFindChildUserTasks(source.getSubProcess(), runTaskKeyList, hasSequenceFlow, userTaskList);
        }

        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);

        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                // 如果为用户任务类型，且任务节点的 Key 正在运行的任务中存在，添加
                if (sequenceFlow.getTargetFlowElement() instanceof UserTask && runTaskKeyList.contains((sequenceFlow.getTargetFlowElement()).getId())) {
                    userTaskList.add((UserTask) sequenceFlow.getTargetFlowElement());
                    continue;
                }
                // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
                if (sequenceFlow.getTargetFlowElement() instanceof SubProcess) {
                    List<UserTask> childUserTaskList = iteratorFindChildUserTasks((FlowElement) (((SubProcess) sequenceFlow.getTargetFlowElement()).getFlowElements().toArray()[0]), runTaskKeyList, hasSequenceFlow, null);
                    // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
                    if (childUserTaskList != null && childUserTaskList.size() > 0) {
                        userTaskList.addAll(childUserTaskList);
                        continue;
                    }
                }
                // 继续迭代
                userTaskList = iteratorFindChildUserTasks(sequenceFlow.getTargetFlowElement(), runTaskKeyList, hasSequenceFlow, userTaskList);
            }
        }
        return userTaskList;
    }

}
