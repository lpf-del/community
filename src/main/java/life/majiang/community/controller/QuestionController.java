package life.majiang.community.controller;

import life.majiang.community.deo.Question;
import life.majiang.community.deo.QuestionDTO;
import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.service.QuestionDtoService;
import life.majiang.community.utilsli.UtilLi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class QuestionController {

    @Autowired
    @SuppressWarnings("all")
    private QuestionDtoService questionDtoService;

    @Autowired
    @SuppressWarnings("all")
    private UtilLi utilLi;

    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper1 questionMapper;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request){
        QuestionDTO questionDTO = questionDtoService.selectById(id);
        Question question = questionMapper.selectById(id);
        question.setViewCount(question.getViewCount()+1);
        questionMapper.updateById(question);
        model.addAttribute("question",questionDTO);
        String time = utilLi.time(questionDTO.getGmtCreate());
        model.addAttribute("time",time);
        System.out.println(request.getSession().getAttribute("user"));
        System.out.println(questionDTO);
        return "question";
    }
    @GetMapping("/publish/{id}")
    public String in(@PathVariable("id") Long id, Model model){
        Question question = questionMapper.selectById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }
}
