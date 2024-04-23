package com.zvz09.xiaochen.mc.component.provider;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zvz09.xiaochen.mc.domain.entity.Account;
import com.zvz09.xiaochen.mc.enums.CloudProviderEnum;
import com.zvz09.xiaochen.mc.enums.ProductEnum;
import com.zvz09.xiaochen.mc.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractProviderClient<R,F> {


    private final AccountMapper accountMapper;

    private final ConcurrentHashMap<String, Account> PROVIDER_ACCOUNT_CACHE = new ConcurrentHashMap<>();


    /**
     * 当前支持云厂商
     */
    public abstract CloudProviderEnum provider() ;

    protected Account getProviderAccount()  {
        Account account = this.PROVIDER_ACCOUNT_CACHE.get(this.provider().getValue() + "_primaryAccount");
        if (account != null) {
            return account;
        }
        account = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getProvider, this.provider().getValue()));
        if (account == null) {
            throw new RuntimeException("未配置有效的账号！");
        }
        this.PROVIDER_ACCOUNT_CACHE.put(this.provider().getValue() + "_primaryAccount", account);
        return account;
    }

    protected abstract R handleClient(F action, String region, ProductEnum productEnum);

    protected abstract R handleClient(F action, String region);
}
