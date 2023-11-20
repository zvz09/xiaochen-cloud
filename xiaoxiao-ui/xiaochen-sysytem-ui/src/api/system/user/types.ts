import { AuthorityVO } from "@/api/system/role/types";

export interface UserVO {
  id: string;
  uuid: string;
  username: string;
  nickName: string;
  sideMode: string;
  headerImg: string;
  baseColor: string;
  activeColor: string;
  authorityId: number;
  authority: AuthorityVO;
  authorities?: any;
  phone: string;
  email: string;
  enable: number;
  sysDepartments?: any;
  sysPositions?: any;
}

export interface UpdateUserInfoParams {
  /* */
  id: number;

  /*用户名 */
  userName?: string;

  /*密码 */
  password?: string;

  /*昵称 */
  nickName?: string;

  /*头型地址 */
  headerImg?: string;

  /*角色id */
  authorityId?: number;

  /*是否启动 */
  enable?: number;

  /*手机号 */
  phone?: string;

  /*邮箱地址 */
  email?: string;
}

// 参数接口
export interface GetUserListParams {
  /*页码 */
  pageNum?: number;

  /*每页大小 */
  pageSize?: number;

  /*关键字 */
  keyword?: string;

  /*用户名 */
  userName?: string;

  /*手机号 */
  phone?: string;
}

// 参数接口
export interface RegisterParams {
  /*用户名 */
  userName?: string;

  /*密码 */
  password?: string;

  /*昵称 */
  nickName?: string;

  /*头型地址 */
  headerImg?: string;

  /*角色id */
  authorityId?: number;

  /*是否启动 */
  enable?: number;

  /*手机号 */
  phone?: string;

  /*邮箱地址 */
  email?: string;
}
