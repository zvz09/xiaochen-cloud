export namespace ReptileParseClass {
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface ReptileParseClassVO {
    id: string;
    createdAt: string;
    updatedAt: string;
    deleted: boolean;
    className: string;
    content: string;
    status: boolean;
  }

  // 参数接口
  export interface ReptileParseClassDTO {
    /* */
    id?: string;

    /*类内容 */
    content?: string;
  }

  export interface DrawerProps {
    title: string;
    row: Partial<ReptileParseClass.ReptileParseClassVO>;
    api?: (params: any) => Promise<any>;
    getTableList?: () => void;
    id?: string;
  }
}
