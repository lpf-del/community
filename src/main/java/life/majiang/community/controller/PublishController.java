package life.majiang.community.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.Question;
import life.majiang.community.deo.User;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.ArticleEntityService;
import life.majiang.community.service.CommentEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PublishController {
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false) String title,
                            @RequestParam(value = "description",required = false) String description,
                            @RequestParam(value = "tag",required = false) String tag,
                            @RequestParam(value = "id",required = false) Long id,
                            HttpServletRequest request,
                            Model model){
        Map<String,Object> map = new HashMap<>();
        map.put("title",title);
        map.put("description",description);
        map.put("tag",tag);
        map.put("id",id);
        model.addAllAttributes(map);
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
        User user = (User)request.getSession().getAttribute("user");

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getAccountId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        if (id==null){
            questionMapper.insert(question);
        }else {
            UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id).eq("gmt_modified",System.currentTimeMillis());
            Question question1 = questionMapper.selectById(id);
            if (question1 == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            questionMapper.update(question,updateWrapper);
        }
        return "redirect:/";
    }

    @Resource
    private ArticleEntityService articleEntityService;

    @GetMapping("/newPublish")
    @ResponseBody
    public String doNewPublish(@RequestParam(value = "title",required = false) String title,
                               @RequestParam(value = "description",required = false) String description,
                               @RequestParam(value = "myTags",required = false) String myTags,
                               @RequestParam(value = "articleType",required = false) String articleType,
                               @RequestParam(value = "releaseForm",required = false) String releaseForm,
                               @RequestParam(value = "fileUrl",required = false) String fileUrl,
                               HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            articleEntityService.addArticle(title, description, myTags, articleType, releaseForm, fileUrl, request);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "发布成功";
    }
}
