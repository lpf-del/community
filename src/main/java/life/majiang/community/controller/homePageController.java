package life.majiang.community.controller;

import life.majiang.community.deo.User;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 个人主页（重构）
 * @date 2021/9/13
 */

@Controller
public class homePageController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    /**
     * 个人主页
     * 首先用缓存
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/homePage")
    public String homePage(HttpServletRequest request, Model model){

        model = userEntityService.getHomePageInformation(request.getCookies(), model);
//        DigestUtils.md5DigestAsHex();
        return "homePage";
    }

    @GetMapping("/personalInformation")
    public String personalInformation(HttpServletRequest request){

        return "personalInformation";
    }

}
