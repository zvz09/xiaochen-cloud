package com.zvz09.xiaochen.common.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zvz09.xiaochen.common.core.page.BasePage;

public interface BaseService<T> extends IService<T> {
   <P extends BasePage,V> IPage<V> page(P p);

}
