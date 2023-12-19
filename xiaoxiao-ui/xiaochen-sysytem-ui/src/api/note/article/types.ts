export namespace Article {
  export interface ArticleDTO {
    /* */
    id?: string;

    /* */
    title?: string;

    /* */
    avatar?: string;

    /* */
    summary?: string;

    /* */
    content?: string;

    /* */
    contentMd?: string;

    /* */
    keywords?: string;

    /* */
    readType?: number;

    /* */
    isStick?: boolean;

    /* */
    isOriginal?: boolean;

    /* */
    originalUrl?: string;

    /* */
    categoryName?: string;

    /* */
    isPublish?: boolean;

    /* */
    isCarousel?: boolean;

    /* */
    isRecommend?: boolean;

    /* */
    tags?: string[];
  }

  export interface ArticleVO {
    id: string;
    createdAt?: any;
    updatedAt?: any;
    deleted?: any;
    userId?: any;
    categoryName?: string;
    title: string;
    avatar?: any;
    summary?: any;
    content?: any;
    contentMd?: any;
    readType?: any;
    isStick?: any;
    isPublish?: any;
    isOriginal: boolean;
    originalUrl: string;
    quantity: number;
    isCarousel?: any;
    isRecommend?: any;
    tags?: any;
  }

  // 参数接口
  export interface SelectArticleListParams {
    /*页码 */
    pageNum?: number;

    /*每页大小 */
    pageSize?: number;

    /*关键字 */
    keyword?: string;
  }

  export interface DrawerProps {
    title: string;
    row: Partial<Article.ArticleVO>;
    api?: (params: any) => Promise<any>;
    getTableList?: () => void;
    id?: string;
  }
}
