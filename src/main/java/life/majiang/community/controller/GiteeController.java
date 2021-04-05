package life.majiang.community.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.GiteeUser;
import life.majiang.community.deo.PrividerToken;
import life.majiang.community.deo.User;
import life.majiang.community.mapper1.UserMapper1;
import life.majiang.community.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
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
    private UserMapper1 userMapper1;

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
        if (giteeUser != null/* && giteeUser.getId()!= null*/) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(giteeUser.getName());
            user.setAccountId(giteeUser.getId());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(giteeUser.getAvatar_url());
            Map<String,Object> map = new HashMap<>();
            map.put("name",giteeUser.getName());
            List<User> users = userMapper1.selectByMap(map);
            if (users!=null&&users.size()==0){
                userMapper1.insert(user);
            }else {
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("account_id",giteeUser.getId());
                userMapper1.update(user,updateWrapper);
            }
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }

    }

}
