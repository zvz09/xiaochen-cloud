// API管理模块
export namespace API {
  export interface ApiVO {
    id?: any;
    path?: any;
    description?: any;
    apiGroup: string;
    method?: any;
    children: ApiVO[];
  }
}
