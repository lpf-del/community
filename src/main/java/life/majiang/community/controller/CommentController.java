package life.majiang.community.controller;

import life.majiang.community.deo.Comment;
import life.majiang.community.deo.CommentDTO;
import life.majiang.community.deo.User;
import life.majiang.community.mapper1.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");

        if (user!=null){
            Comment comment = new Comment();
            comment.setParentId(commentDTO.getParentId());
            comment.setContent(commentDTO.getContent());
            comment.setType(commentDTO.getType());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(comment.getGmtCreate());
            comment.setCommentator(user.getId());
            commentMapper.insert(comment);
        }else {
            return "redirect:/";
        }

        return null;
    }
}
