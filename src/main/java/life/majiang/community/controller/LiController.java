package life.majiang.community.controller;

import life.majiang.community.deo.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class LiController {
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;
    public String huifu(@PathVariable("id") Long id,
                            HttpServletRequest request,
                            Model model){

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0){
            model.addAttribute("error","用户未登录");
            return "huifu";
        }
        String value = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                value = cookie.getValue();
                break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("token",value);
        List<User> users = userMapper1.selectByMap(map);
        User user = new User();
        if (users.size()!=0){
            user = users.get(0);
            request.getSession().setAttribute("user", user);
        }else {
            model.addAttribute("error","用户未登录");
            return "huifu";
        }


        return "redirect:/";
    }
    @GetMapping("/delect/{id}")
    public String delect(@PathVariable("id") Long id){
        questionMapper.deleteById(id);
        return "redirect:/";
    }
}
