// 请求响应参数（不包含data）
export interface Result {
  code: number;
  success: boolean;
  msg: string;
}

// 请求响应参数（包含data）
export interface ResultData<T = any> extends Result {
  /*响应结果数据 */
  data: T;
  /*响应编码 200：成功，500：失败 */
  code: number;

  /*响应结果 true：成功，false：失败 */
  success: boolean;

  /*响应消息 */
  msg: string;
}

// 分页响应参数
export interface ResPage<T> {
  records: T[];
  current: number;
  size: number;
  total: number;
}

export interface ESResPage<T> {
  records: T[];
  pageNum: number;
  pageSize: number;
  total: number;
}

// 分页请求参数
export interface ReqPage {
  pageNum: number;
  pageSize: number;
}

// 文件上传模块
export namespace Upload {
  export interface ResFileUrl {
    fileUrl: string;
  }
}

// 登录模块
export namespace Login {
  export interface ReqLoginForm {
    username: string;
    password: string;
  }

  export interface ResLogin {
    user: any;
    token: string;
  }

  export interface ResAuthButtons {
    [key: string]: string[];
  }
}

// 用户管理模块
export namespace User {
  export interface ReqUserParams extends ReqPage {
    username: string;
    gender: number;
    idCard: string;
    email: string;
    address: string;
    createTime: string[];
    status: number;
  }

  export interface ResUserList {
    id: string;
    username: string;
    gender: number;
    user: {
      detail: {
        age: number;
      };
    };
    idCard: string;
    email: string;
    address: string;
    createTime: string;
    status: number;
    avatar: string;
    photo: any[];
    children?: ResUserList[];
  }

  export interface ResStatus {
    userLabel: string;
    userValue: number;
  }

  export interface ResGender {
    genderLabel: string;
    genderValue: number;
  }

  export interface ResDepartment {
    id: string;
    name: string;
    children?: ResDepartment[];
  }

  export interface ResRole {
    id: string;
    name: string;
    children?: ResDepartment[];
  }
}
