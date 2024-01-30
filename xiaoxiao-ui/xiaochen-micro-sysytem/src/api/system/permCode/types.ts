// 权限字管理模块
import { API } from "@/api/system/api/types";
import { Role } from "@/api/system/role/types";

export namespace PermCode {
  // 参数接口
  export interface ListTreeParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  // 参数接口
  export interface CreateParams {
    /* */
    id: number;

    /*菜单Id */
    menuId?: number;

    /*上级权限字Id */
    parentId?: number;

    /*权限字标识(一般为有含义的英文字符串) */
    permCode?: string;

    /*类型(0: 菜单 1: UI片段 2: 操作) */
    permCodeType?: string;

    /*显示名称 */
    showName?: string;

    /*显示顺序(数值越小，越靠前) */
    showOrder?: number;

    /*资源ID */
    apiIds?: Record<string, unknown>[];
  }

  // 参数接口
  export interface UpdateParams {
    /* */
    id: number;

    /*菜单Id */
    menuId?: number;

    /*上级权限字Id */
    parentId?: number;

    /*权限字标识(一般为有含义的英文字符串) */
    permCode?: string;

    /*类型(0: 菜单 1: UI片段 2: 操作) */
    permCodeType?: string;

    /*显示名称 */
    showName?: string;

    /*显示顺序(数值越小，越靠前) */
    showOrder?: number;
  }

  export interface PermCodeVO {
    id: string;
    parentId: string;
    permCode: string;
    permCodeType: string;
    showName: string;
    showOrder: number;
    children: PermCodeVO[];
    /*资源ID */
    apiIds?: string[];
    apiVos?: API.ApiVO[];
  }

  export interface DrawerProps {
    title: string;
    row: Partial<Role.RoleVO>;
    api?: (params: any) => Promise<any>;
    getTableList?: () => void;
    id?: string;
  }
}
