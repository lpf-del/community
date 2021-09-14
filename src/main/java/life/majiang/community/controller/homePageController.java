package life.majiang.community.controller;

import life.majiang.community.deo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 个人主页（重构）
 * @date 2021/9/13
 */

@Controller
public class homePageController {

    @GetMapping("/homePage")
    public String homePage(HttpServletRequest request){
//        User user = (User)request.getSession().getAttribute("user");
//        user.getName();
        return "homePage";
    }

    @GetMapping("/personalInformation")
    public String personalInformation(){
        return null;
    }

}
