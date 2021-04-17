package life.majiang.community.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.*;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper1.CommentMapper;
import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.mapper1.UserMapper1;
import life.majiang.community.mapper1.UtilLpfMapper;
import life.majiang.community.service.QuestionDtoService;
import life.majiang.community.utilsli.UtilLi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Autowired
    @SuppressWarnings("all")
    private UtilLpfMapper utilLpfMapper;

    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    @Autowired
    @SuppressWarnings("all")
    private UserMapper1 userMapper1;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model, HttpServletRequest request){
        QuestionDTO questionDTO = questionDtoService.selectById(id);
        Question question = questionMapper.selectById(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<Question>();
        updateWrapper.setSql("view_count = view_count +1").eq("id",question.getId());
        questionMapper.update(null,updateWrapper);
        model.addAttribute("question",questionDTO);
        String time = utilLi.time(questionDTO.getGmtCreate());
        model.addAttribute("time",time);

        List<Question> questionList= utilLi.selectRelated(questionDTO);
        System.out.println(questionList);
       model.addAttribute("relatedQuestions",questionList);


        List<CommentUserDTO> list = utilLi.listByQuestionId(id,0L);

        model.addAttribute("comments",list);

        Map<String,Object> map = new HashMap<>();
        map.put("wen_zhang_id",question.getId());
        List<Utillpf> utilLpfs = utilLpfMapper.selectByMap(map);
        if (utilLpfs.size()==1&&utilLpfs!=null){
            model.addAttribute("zan",true);
        }else {
            model.addAttribute("zan",false);
        }

        return "question";
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO comment(@PathVariable(name = "id") Long id){
        List<CommentUserDTO> list = utilLi.listByQuestionId(id,1L);
        return ResultDTO.ok(list);
    }
    @GetMapping("/publish/{id}")
    public String in(@PathVariable("id") Long id, Model model){
        Question question = questionMapper.selectById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }
    @GetMapping("/dianzan1/{id}/{idi}")
    public String dianzan(@PathVariable("id") Long id, @PathVariable("idi") Long idi, HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");

        Map<String,Object> map = new HashMap<>();
        map.put("user_name",user.getName());
        List<Utillpf> utillpfs = utilLpfMapper.selectByMap(map);

        if (user==null || utillpfs.size() != 0){
            model.addAttribute("zan1",false);
            return "redirect:/question/{id}";
        }

        Comment comment = commentMapper.selectById(idi);

        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("like_count = like_count + 1").eq("id",idi);
        commentMapper.update(null,updateWrapper);

        Utillpf utilLpf = new Utillpf();
        utilLpf.setUserName(user.getName());
        utilLpf.setWenZhangId(comment.getId());
        utilLpf.setDianZan(true);
        utilLpfMapper.insert(utilLpf);

        model.addAttribute("zan1",true);
        return "redirect:/question/{id}";
    }
    @GetMapping("/dianzan/{id}")
    public String dianzan1(@PathVariable("id") Long id, HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");

        Map<String,Object> map = new HashMap<>();
        map.put("user_name",user.getName());
        List<Utillpf> utillpfs = utilLpfMapper.selectByMap(map);

        if (user==null || utillpfs.size() != 0){
            model.addAttribute("zan",false);
            return "redirect:/question/{id}";
        }

        Question question = questionMapper.selectById(id);

        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("like_count = like_count + 1").eq("id",id);
        questionMapper.update(null,updateWrapper);

        Utillpf utilLpf = new Utillpf();
        utilLpf.setUserName(user.getName());
        utilLpf.setWenZhangId(question.getId());
        utilLpf.setDianZan(true);
        utilLpfMapper.insert(utilLpf);

        model.addAttribute("zan",true);
        return "redirect:/question/{id}";
    }
}
