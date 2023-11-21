// 角色管理模块
export namespace Role {
  // 参数接口
  export interface CreateRoleParams {
    /* */
    id: number;

    /*角色编码 */
    authorityCode: string;

    /*角色名称 */
    authorityName: string;
  }

  // 参数接口
  export interface UpdateRoleParams {
    /* */
    id: number;

    /*角色编码 */
    authorityCode: string;

    /*角色名称 */
    authorityName: string;
  }
  // 参数接口
  export interface CopyRoleParams {
    /* */
    authority: {
      /* */
      id: Record<string, unknown>;

      /*角色编码 */
      authorityCode: string;

      /*角色名称 */
      authorityName: string;
    };

    /*旧角色ID */
    oldRoleId: number;
  }

  // 参数接口
  export interface GetRoleListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface RoleVO {
    id?: string;
    roleCode: string;
    roleName: string;
  }
  // 响应接口
  export interface ListPermCodesRes {
    [key: string]: string[];
  }
}
