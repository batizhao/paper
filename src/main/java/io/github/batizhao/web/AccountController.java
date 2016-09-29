package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import io.github.batizhao.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Controller
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        Iterable<Account> accounts = accountService.findAll();
        model.addAttribute("accounts", accounts);
        return "account/index";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "account/create-modal";
    }

}
