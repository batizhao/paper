package io.github.batizhao.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author batizhao
 * @since 2016/9/29
 */
@Controller
@RequestMapping("role")
public class RoleController {

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "role/index";
    }
}
