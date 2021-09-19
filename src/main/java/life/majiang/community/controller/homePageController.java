package life.majiang.community.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.PersonService;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    @Resource
    private PersonService personService;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private CookieService cookieService;

    /**
     * 个人主页
     * 首先用缓存
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/homePage")
    public String homePage(HttpServletRequest request, Model model){

        model = userEntityService.getHomePageInformation(request, model);
//        DigestUtils.md5DigestAsHex();
        return "homePage";
    }

    /**
     * 个人主页（个人资料）
     * @param request
     * @param model
     * @param xiugai 为0是正常页面， 为1是修改页面
     * @return
     */
    @GetMapping("/personalInformation")
    public String personalInformation(HttpServletRequest request, Model model, Integer xiugai){
        UserEntity userEntity = personService.getPersonInformation(request, model);
        model.addAttribute("userEntity", userEntity);
        if (xiugai == 1) model.addAttribute("xiugai", xiugai);
        return "personalInformation";
    }


    /**
     * 个人主页（个人资料）
     * 只允许修改用户名， 简介， 地址
     * @param username
     * @param introduction
     * @param adress
     * @param request
     * @return
     */
    @PostMapping("/modifyInformation")
    private String telephoneLogin(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "introduction", required = false) String introduction,
                                  @RequestParam(value = "address", required = false) String adress,
                                  HttpServletRequest request){
        UserEntity personInformation = cookieService.getPersonInformation(request);
        if (username == null || username.equals("")) username = personInformation.getUserName();
        if (introduction == null || introduction.equals("")) introduction = personInformation.getIntroduction();
        if (adress == null || adress.equals("")) adress = personInformation.getAddress();
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("user_name", username);
        updateWrapper.set("address", adress);
        updateWrapper.set("introduction", introduction);
        userEntityMapper.update(personInformation, updateWrapper);

        return "redirect:/personalInformation?xiugai=0";

    }

}
