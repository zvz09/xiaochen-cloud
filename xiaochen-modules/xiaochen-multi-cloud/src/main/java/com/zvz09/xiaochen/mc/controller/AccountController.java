package com.zvz09.xiaochen.mc.controller;

import com.zvz09.xiaochen.common.web.controller.BaseController;
import com.zvz09.xiaochen.mc.domain.dto.AccountDTO;
import com.zvz09.xiaochen.mc.domain.entity.Account;
import com.zvz09.xiaochen.mc.service.IAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mc/account")
@Tag(name = "云厂商账号管理")
public class AccountController extends BaseController<IAccountService, Account, AccountDTO> {

    public AccountController(IAccountService accountService) {
        super(accountService);
    }
}
