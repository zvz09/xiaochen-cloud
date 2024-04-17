export namespace Tags {
  // 参数接口
  export interface TagsDTO {
    /* */
    id?: string;
    /*标签名称 */
    name?: string;
    /* */
    clickVolume?: number;
    /*排序 */
    sort?: number;
  }

  export interface TagsVO {
    /* */
    id: string;
    /*标签名称 */
    name: string;
    /* */
    clickVolume?: number;
    /*排序 */
    sort?: number;
  }

  export interface ListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }
}
