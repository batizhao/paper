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

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Account save(@ModelAttribute("account") Account account){
        return accountService.save(account);
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model){
        Account account = accountService.findOne(id);
        model.addAttribute("account", account);
        return "account/create-modal";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Account update(@ModelAttribute("account") Account account){
        return accountService.update(account);
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public boolean delete(@PathVariable Long id){
        accountService.delete(id);
        return true;
    }

}
