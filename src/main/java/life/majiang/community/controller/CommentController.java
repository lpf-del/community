package life.majiang.community.controller;

import life.majiang.community.deo.*;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.util.UtilLi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;

    @Autowired
    @SuppressWarnings("all")
    private UtilLi utilLi;

    @Autowired
    @SuppressWarnings("all")
    private NotificationMapper notificationMapper;

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        Map<String,Object> map = utilLi.getmap(commentDTO,request);

        User user = (User)map.get("user");

        if (user == null){
            return ResultDTO.errorOf(203L,"未登录不能进行评论,请先登录");
        }

        String message = (String)map.get("message");
        if (message == null || message == ""){
            Comment comment = new Comment();
            comment.setParentId(commentDTO.getParentId());
            comment.setContent(commentDTO.getContent());
            comment.setType(commentDTO.getType());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(comment.getGmtCreate());
            comment.setCommentator(user.getId());
            comment.setLikeCount(0L);
            commentMapper.insert(comment);

            utilLi.countli(commentDTO);
        }

        utilLi.tongzhi(commentDTO.getType(),commentDTO,request);


        return map;
    }
}








