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
 * @since 2016/9/29
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        Account account = accountService.findByUsername("admin");
        model.addAttribute("account", account);
        return "dashboard";
    }

}
