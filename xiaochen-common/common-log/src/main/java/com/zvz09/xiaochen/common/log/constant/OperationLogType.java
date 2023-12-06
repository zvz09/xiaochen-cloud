package com.zvz09.xiaochen.common.log.constant;

/**
 * 操作日志类型常量字典对象。
 *
 * @author zvz09
 */
public final class OperationLogType {

    /**
     * 其他。
     */
    public static final String OTHER = "其他";
    /**
     * 登录。
     */
    public static final String LOGIN = "登录";
    /**
     * 登出。
     */
    public static final String LOGOUT = "登出";
    /**
     * 新增。
     */
    public static final String ADD = "新增";
    /**
     * 修改。
     */
    public static final String UPDATE = "修改";
    /**
     * 删除。
     */
    public static final String DELETE = "删除";
    /**
     * 批量删除。
     */
    public static final String DELETE_BATCH = "批量删除";
    /**
     * 查询。
     */
    public static final String LIST = "查询";
    /**
     * 导出。
     */
    public static final String EXPORT = "导出";
    /**
     * 导入。
     */
    public static final String IMPORT = "导入";
    /**
     * 上传。
     */
    public static final String UPLOAD = "上传";
    /**
     * 下载。
     */
    public static final String DOWNLOAD = "下载";

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private OperationLogType() {
    }
}
