package life.majiang.community.controller;

import life.majiang.community.deo.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.service.QuestionDtoService;
import life.majiang.community.service.QuestionService;
import life.majiang.community.utilsli.UtilLi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


@Controller
public class QuestionController {

    @Autowired
    @SuppressWarnings("all")
    private QuestionDtoService questionDtoService;

    @Autowired
    @SuppressWarnings("all")
    private UtilLi utilLi;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        QuestionDTO questionDTO = questionDtoService.selectById(id);
        model.addAttribute("question",questionDTO);
        String time = utilLi.time(questionDTO.getGmtCreate());
        model.addAttribute("time",time);
        return "question";
    }
}
