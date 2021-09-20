package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.GiteeUser;
import life.majiang.community.deo.PrividerToken;
import life.majiang.community.deo.User;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.provider.GiteeProvider;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class GiteeController {
    @Autowired
    private PrividerToken prividerToken;
    @Value("${gitee.oauth.clientid}")
    public String CLIENTID;
    @Value("${gitee.oauth.clientsecret}")
    public String CLIENTSECRET;
    @Value("${gitee.oauth.callback}")
    public String URL;
    @Autowired
    private GiteeProvider giteeProvider;
    @Autowired
    private GiteeUser giteeUser;
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;
    @Resource
    private UserEntityService userEntityService;  //新user类

    @Resource
    private RedisUtil redisUtil; //redis工具类

    @Resource
    private CookieService cookieService;


    @GetMapping("/log")
    public String loin(HttpServletRequest request, Model model) {
        model.addAttribute("st1", 1);
        return "login";
    }

    @GetMapping("/log2")
    public String loin2(HttpServletRequest request, Model model) {
        model.addAttribute("st2", 2);
        return "login";
    }
    @GetMapping("/log3")
    public String loin3(HttpServletRequest request, Model model) {
        model.addAttribute("st3", 3);
        return "login";
    }

    /**
     * 跳转注册界面
     *
     * @return
     */
    @GetMapping("/toRegister")
    public String toRegister(HttpServletRequest request, Model model) {

        return "register";
    }

    /**
     * 注册初始化 点赞、热度、访问量、uuid、注册时间
     * username不能重复、其他不做限制
     *
     * 密码使用MD5加密
     *
     * 注册添加cookie、并跳转主页面、在redis添加手机方便手机登录
     *
     * redis添加用户信息（key： 电话号  value：userRntity类（json））
     *
     * @param username
     * @param password
     * @param telephone
     * @param response
     * @param model
     * @param email
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "telephone", required = false) String telephone,
                           @RequestParam(value = "email", required = false) String email,
                           HttpServletResponse response,
                           Model model) {

        try {
            UserEntity userEntity = userEntityService.register(username,password,telephone,email);
            cookieService.addUserToken(response, telephone, password);
            redisUtil.set(telephone, JSON.toJSONString(userEntity));
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error",e.getMessage());
            return "register";
        }

    }

    /**
     * 手机 + 密码登录
     * @param password
     * @param telephone
     * @param response
     * @param model
     * @return
     */
    @PostMapping("/telephoneLogin")
    private String telephoneLogin(@RequestParam(value = "password", required = false) String password,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  HttpServletResponse response,
                                  Model model){
        try {
            UserEntity userEntity = userEntityService.telephoneLogin(password, telephone);
            cookieService.addUserToken(response, telephone, password);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code
            , @RequestParam("state") String state
            , HttpServletResponse response) {
        prividerToken.setClient_id(CLIENTID);
        prividerToken.setCode(code);
        prividerToken.setRedirect_uri(URL);
        prividerToken.setClient_secret(CLIENTSECRET);
        prividerToken.setState(state);
        String accessToken = giteeProvider.getAccessToken(prividerToken);
        giteeUser = giteeProvider.getUser(accessToken);
        if (giteeUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(giteeUser.getName());
            user.setAccountId(giteeUser.getId());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(giteeUser.getAvatar_url());
            Map<String, Object> map = new HashMap<>();
            map.put("name", giteeUser.getName());
            List<User> users = userMapper1.selectByMap(map);
            if (users != null && users.size() == 0) {
                userMapper1.insert(user);
            } else {
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("account_id", giteeUser.getId());
                userMapper1.update(user, updateWrapper);
            }
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }

    }

}
