package life.majiang.community.controller;

import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.mapper1.UserMapper1;
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
    private UserMapper1 userMapper1;
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper1 questionMapper;
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
        request.getSession().removeAttribute("user");
        Cookie remCookie = new Cookie("token",null);
        remCookie.setMaxAge(0);
        response.addCookie(remCookie);
        return "redirect:/";
    }

}
