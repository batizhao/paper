package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author batizhao
 * @since 2016/9/29
 */
@Controller
public class LoginController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final String PATH_INDEX = "login";

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String index() {
        return PATH_INDEX;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String authenticate(Account account, Model model, HttpServletRequest request) {

        UsernamePasswordToken token = new UsernamePasswordToken(account.getUsername(), account.getPassword());
        logger.info("Authenticating : {}", token.getUsername());
        final Subject subject = SecurityUtils.getSubject();

        try {
            //会调用 ShiroDbRealm 的认证方法 doGetAuthenticationInfo(AuthenticationToken)
            subject.login(token);
        } catch (UnknownAccountException uae) {
            model.addAttribute("message", "该账号不存在！");
            return PATH_INDEX;
        } catch (IncorrectCredentialsException ice) {
            model.addAttribute("message", "用户名或密码错误，请重新输入！");
            return PATH_INDEX;
        }  catch (Exception e) {
            model.addAttribute("message", "未知错误，请联系管理员。");
            logger.error("登录未知错误", e);
            return PATH_INDEX;
        }

        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        // 获取保存的URL
        if (savedRequest == null || savedRequest.getRequestUrl() == null) {
            return "redirect:/dashboard";
        }
        return "redirect:" + savedRequest.getRequestUrl();
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }

}
