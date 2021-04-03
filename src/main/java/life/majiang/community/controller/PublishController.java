package life.majiang.community.controller;

import life.majiang.community.deo.Question;
import life.majiang.community.deo.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PublishController {
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null && title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null && description == ""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if (tag == null && tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0){
            model.addAttribute("error","用户未登录");
            return "publish";
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
        List<User> users = userMapper.selectByMap(map);
        User user = new User();
        if (users.size()!=0){
            user = users.get(0);
            request.getSession().setAttribute("user", user);
        }else {
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(Long.parseLong(user.getAccountId()));
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.insert(question);
        return "redirect:/";
    }
}
