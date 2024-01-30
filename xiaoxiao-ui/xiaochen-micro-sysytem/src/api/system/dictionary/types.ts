export namespace Dictionary {
  // 参数接口
  export interface GetSysDictionaryListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*字典名（中） */
    name?: string;

    /*字典名（英） */
    encode?: string;

    /*状态 */
    status?: boolean;
  }
  // 参数接口
  export interface GetByDictionaryTypeParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }
  // 参数接口
  export interface GetSysDictionaryDetailListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;

    /*字典Id */
    sysDictionaryId: string;

    /*展示值 */
    label?: string;

    /*字典值 */
    value?: number;

    /*启用状态 */
    status?: boolean;
  }

  // 参数接口
  export interface DictionaryVO {
    /* */
    id?: string;

    /*字典名（中） */
    name?: string;

    /*字典名（英） */
    encode?: string;

    /*状态 */
    status?: boolean;

    /*描述 */
    description?: string;
  }

  // 参数接口
  export interface DictionaryDetailVO {
    /* */
    id?: string;

    /*展示值 */
    label: string;

    /*字典值 */
    value: string;

    /*启用状态 */
    status?: boolean;

    /*排序 */
    sort?: number;

    /* */
    tagType?: string;

    /*字典id */
    sysDictionaryId?: string;
  }
}
