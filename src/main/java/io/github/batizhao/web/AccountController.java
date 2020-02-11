package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import io.github.batizhao.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@RestController
@RequestMapping("account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("{username}")
    public Account findByUsername(@PathVariable String username) {
        Account account = accountService.findByUsername(username);
        return account;
    }

    @GetMapping("index")
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

    @DeleteMapping("{id:\\d+}")
    public boolean doDelete(@PathVariable Long id) {
        try {
            accountService.delete(id);
            return true;
        } catch (Exception e) {
            log.error("Delete Account.id = {} failed.", id, e);
            return false;
        }
    }

    @GetMapping("role")
    public Iterable<Account> doFindByRoles(@RequestParam("role") String role) {
        Iterable<Account> accounts = accountService.findByRoles(role);
        return accounts;
    }

}
