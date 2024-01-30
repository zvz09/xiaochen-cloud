export type MenuVO = {
  id: string;
  parentId: string;
  path: string;
  name: string;
  component?: string;
  sort: number;
  redirect?: string;
  meta: MetaProps;
  children?: MenuVO[];
};

export type MetaProps = {
  icon: string;
  title: string;
  activeMenu?: string;
  isLink?: string;
  isHide: boolean;
  isFull: boolean;
  isAffix: boolean;
  isKeepAlive: boolean;
};
/**
 * 菜单表单对象类型
 */
export type MenuForm = {
  id?: string;
  parentId?: string;
  path: string;
  name: string;
  component?: string;
  sort: number;
  redirect?: string;
  icon: string;
  title: string;
  activeMenu?: string;
  isLink?: string;
  isHide: boolean;
  isFull: boolean;
  isAffix: boolean;
  isKeepAlive: boolean;
};
