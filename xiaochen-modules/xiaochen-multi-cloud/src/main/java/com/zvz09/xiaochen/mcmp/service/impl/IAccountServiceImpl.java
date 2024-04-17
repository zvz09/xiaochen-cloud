package com.zvz09.xiaochen.mcmp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.mcmp.domain.entity.Account;
import com.zvz09.xiaochen.mcmp.mapper.AccountMapper;
import com.zvz09.xiaochen.mcmp.service.IAccountService;
import org.springframework.stereotype.Service;

/**
* @author lizili-YF0033
* @description 针对表【mcmp_primary_account(主账号表)】的数据库操作Service实现
* @createDate 2024-03-11 14:15:50
*/
@Service
public class IAccountServiceImpl  extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    public <P extends BasePage, V> IPage<V> page(P p) {
        return null;
    }
}




