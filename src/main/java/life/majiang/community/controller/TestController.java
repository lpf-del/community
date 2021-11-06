package life.majiang.community.controller;

import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserEntityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lpf
 * @description 用于展示系统,对于系统免登录，扫描进入系统
 * @date 2021/11/4
 */
@Controller
public class TestController {

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private CookieService cookieService;

    @GetMapping("/test")
    public String testLogin(String code,HttpServletResponse response) throws Exception {
        if (!code.equals("shihdisdnjcbd")) return "redirect:/";
        userEntityService.telephoneLogin("3773736639823827", "19862125285");
        cookieService.addUserToken(response, "19862125285", "3773736639823827");
        return "redirect:/";
    }

}
