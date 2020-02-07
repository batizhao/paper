package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import io.github.batizhao.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public Iterable<Account> index(){
        Iterable<Account> accounts = accountService.findAll();
        System.out.println(accounts.toString());
        return accounts;
    }

}
