export namespace Process {
  // 参数接口
  export interface TodoProcessListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*流程标识 */
    processKey?: string;

    /*流程名称 */
    processName?: string;

    /*流程分类 */
    category?: string;

    /*状态 */
    state?: string;

    /*请求参数 */
    params?: Record<string, unknown>;
  }

  export interface ProcVar {}

  export interface TaskLocalVar {}

  export interface Comment {
    type: string;
    comment: string;
  }

  export interface CommentList {
    type: string;
    id: string;
    time: string;
    taskId: string;
    userId: string;
    processInstanceId: string;
    fullMessage: string;
  }

  export interface FlowableTaskVo {
    taskId: string;
    taskName: string;
    taskDefKey: string;
    assigneeId: number;
    deptName: string;
    startDeptName: string;
    assigneeName: string;
    startUserId: number;
    startUserName: string;
    category: string;
    procVars: ProcVar;
    taskLocalVars: TaskLocalVar;
    deployId: string;
    procDefId: string;
    procDefKey: string;
    procDefName: string;
    procDefVersion: number;
    procInsId: string;
    hisProcInsId: string;
    duration: string;
    comment: Comment;
    commentList: CommentList[];
    candidate: string;
    createTime: string;
    claimTime: string;
    finishTime: string;
    processStatus: string;
  }

  // 参数接口
  export interface StartProcessListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*流程标识 */
    processKey?: string;

    /*流程名称 */
    processName?: string;

    /*流程分类 */
    category?: string;

    /*状态 */
    state?: string;

    /*请求参数 */
    params?: Record<string, unknown>;
  }
  export interface FlowableDeployVo {
    definitionId: string;
    processName: string;
    processKey: string;
    category: string;
    version: number;
    formId: number;
    formName: string;
    deploymentId: string;
    suspended: boolean;
    deploymentTime: string;
  }
  // 参数接口
  export interface StartParams {
    /* */
    processDefId?: string;

    /* */
    variables?: Record<string, unknown>;
  }

  // 参数接口
  export interface ProcessTaskPageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*流程标识 */
    processKey?: string;

    /*流程名称 */
    processName?: string;

    /*流程分类 */
    category?: string;

    /*状态 */
    state?: string;

    /*请求参数 */
    params?: Record<string, unknown>;
  }

  // 参数接口
  export interface CopyProcessListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /* */
    processName?: string;

    /* */
    userId?: number;

    /* */
    originatorName?: string;
  }

  export interface FlowableCopyVo {
    copyId: number;
    title: string;
    processId: string;
    processName: string;
    categoryId: string;
    deploymentId: string;
    instanceId: string;
    taskId: string;
    userId: number;
    originatorId: number;
    originatorName: string;
    createTime: string;
  }

  export interface TaskFormData {
    formName: string;
    formTemplate: string;
    models: string;
  }

  export interface CommentList {
    type: string;
    id: string;
    time: string;
    taskId: string;
    userId: string;
    processInstanceId: string;
    fullMessage: string;
  }

  export interface HistoryProcNodeList {
    procDefId: string;
    activityId: string;
    activityName: string;
    activityType: string;
    duration: string;
    assigneeId: number;
    assigneeName: string;
    candidate: string;
    commentList: CommentList[];
    createTime: string;
    endTime: string;
  }

  export interface ProcessFormList {
    formName: string;
    formTemplate: string;
    models: string;
  }

  export interface FlowViewer {
    finishedTaskSet: any[];
    finishedSequenceFlowSet: any[];
    unfinishedTaskSet: any[];
    rejectedTaskSet: any[];
  }

  export interface FlowableDetailVo {
    taskFormData: TaskFormData;
    historyProcNodeList: HistoryProcNodeList[];
    processFormList: ProcessFormList[];
    bpmnXml: string;
    flowViewer: FlowViewer;
    existTaskForm: boolean;
  }
}
