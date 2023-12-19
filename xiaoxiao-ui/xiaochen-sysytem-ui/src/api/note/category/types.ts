export namespace Category {
  export interface CategoryDTO {
    /* */
    id?: string;
    /*分类名称 */
    name?: string;

    /* */
    clickVolume?: number;

    /*排序 */
    sort?: number;

    /*图标 */
    icon?: string;
  }

  export interface CategoryVO {
    /* */
    id: string;
    /*分类名称 */
    name: string;

    /* */
    clickVolume?: number;

    /*排序 */
    sort?: number;

    /*图标 */
    icon?: string;
  }

  // 参数接口
  export interface PageParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }
}
