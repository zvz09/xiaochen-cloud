import { RouteLocationNormalized } from "vue-router";

export const fmtTitle = (title: string, now: RouteLocationNormalized) => {
  if (!title) return title;
  const reg = /\$\{(.+?)\}/;
  const reg_g = /\$\{(.+?)\}/g;
  const result = title.match(reg_g);
  if (result) {
    result.forEach(item => {
      const key = item.match(reg)[1] as string;
      const value = (now.params[key] as string) || (now.query[key] as string);
      title = title.replace(item, value);
    });
  }
  return title;
};
