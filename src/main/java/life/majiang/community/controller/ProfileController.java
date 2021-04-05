package life.majiang.community.controller;

import life.majiang.community.deo.User;
import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.mapper1.UserMapper1;
import life.majiang.community.service.PageDTO;
import life.majiang.community.service.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper1 userMapper1;
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper1 questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private QuestionDtoService questionDtoService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page){

        if ("question".equals(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }
        if ("repies".equals(action)){
            model.addAttribute("section","repies");
            model.addAttribute("sectionName","最新回复");
        }

        User user= (User)request.getSession().getAttribute("user");
        if (user!=null){
            PageDTO pageDTO = questionDtoService.Listwen(page,user.getAccountId());
            model.addAttribute("pageDTO",pageDTO);
        }else {
            return "redirect:/";
        }

        return "profile";
    }
}
