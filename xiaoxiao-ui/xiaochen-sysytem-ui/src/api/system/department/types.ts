export namespace Department {
  export interface DTO {
    /* */
    id?: string;
    /*父部门id */
    parentId?: string;
    /*部门名称 */
    deptName: string;
    /*显示顺序 */
    orderNum?: number;
    /*负责人 */
    leader: string;
    /*联系电话 */
    phone: string;
    /*邮箱 */
    email: string;
    /*部门状态0正常1停用 */
    status: boolean;
  }

  export interface VO {
    id: string;
    parentId: string;
    deptName: string;
    orderNum: number;
    leader: string;
    phone: string;
    email: string;
    status: boolean;
    children: VO[];
  }
}
