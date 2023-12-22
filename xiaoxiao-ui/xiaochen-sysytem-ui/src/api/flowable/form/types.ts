export namespace Form {
  // 参数接口
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  // 参数接口
  export interface ListParams {
    /* */
    formName?: string;

    /* */
    formContent?: string;

    /* */
    thumbnail?: string;
  }

  export interface VO {
    id: string;
    createdAt: string;
    updatedAt: string;
    deleted: boolean;
    formName: string;
    formContent: string;
    thumbnail: string;
  }

  export interface DTO {
    id?: string;
    formName: string;
    formContent: string;
    thumbnail: string;
  }

  export interface DrawerProps {
    title: string;
    row: Partial<Form.VO>;
    api?: (params: any) => Promise<any>;
    getTableList?: () => void;
    id?: string;
  }
}
