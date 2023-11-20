package com.zvz09.xiaochen.common.core.constant;

/**
 * 公共常量
 * CommonConstant
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/8/25 15:54
 */
public interface CommonConstant {

    /**
     * 默认页码为1
     */
    Integer DEFAULT_PAGE_INDEX = 1;

    /**
     * 默认页大小为10
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 分页总行数名称
     */
    String PAGE_TOTAL_NAME = "total";

    /**
     * 分页数据列表名称
     */
    String PAGE_DATA_NAME = "list";

    /**
     * 分页当前页码名称
     */
    String PAGE_INDEX_NAME = "pageIndex";

    /**
     * 分页当前页大小名称
     */
    String PAGE_SIZE_NAME = "pageSize";

    String COMMA = ",";

    /**
     * 请求的原始字符串
     */
    String REQUEST_PARAM_BODY_STRING = "REQUEST_PARAM_BODY_STRING";

    /**
     * 日志链路ID
     */
    String TRACE_ID = "traceId";

    /**
     * 1000
     */
    int ONE_THOUSAND = 1000;

    /**
     *
     */
    String LOGIN_USER_INFO = "login_user_info";

    /**
     *
     */
    String LOGIN_USER_AUTHORITY_ID = "login_user_role_Id";
}
