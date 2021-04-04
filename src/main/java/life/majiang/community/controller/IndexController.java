package life.majiang.community.controller;

import life.majiang.community.deo.Question;
import life.majiang.community.deo.QuestionDTO;
import life.majiang.community.deo.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.PageDTO;
import life.majiang.community.service.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper;
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private QuestionDtoService questionDtoService;
    @GetMapping(value = {"/"})
    public String index(HttpServletRequest request
                        , Model model
                        , @RequestParam(name = "page",defaultValue = "1") Integer page){
        PageDTO pageDTO = questionDtoService.List(page);

        model.addAttribute("pageDTO",pageDTO);
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        request.getSession().invalidate();
        Cookie remCookie = new Cookie("token","");
        remCookie.setMaxAge(0);
        response.addCookie(remCookie);
        return "redirect:/";
    }

}
