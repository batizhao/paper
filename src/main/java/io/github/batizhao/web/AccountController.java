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

    @GetMapping("/{username}")
    public Account findByUsername(@PathVariable String username) {
        Account account = accountService.findByUsername(username);
        return account;
    }

    @GetMapping
    public Iterable<Account> findAll() {
        Iterable<Account> accounts = accountService.findAll();
        return accounts;
    }

    @PostMapping
    public Account doSaveOrUpdate(@RequestBody Account request_account) {
        Account account;
        if (request_account.getId() != null) {
            account = accountService.update(request_account);
        } else {
            account = accountService.save(request_account);
        }
        return account;
    }

}
