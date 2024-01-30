export namespace Dictionary {
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
