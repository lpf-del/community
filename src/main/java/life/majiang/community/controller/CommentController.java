package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.deo.*;
import life.majiang.community.entity.CommentAndUser;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.service.*;
import life.majiang.community.util.UtilLi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @Resource
    private CommentEntityService commentEntityService;

    @Resource
    private CookieService cookieService;

    @Resource
    private UserCommentLogEntityService userCommentLogEntityService;

    /**
     * 获取某一文章的5条评论，使用ajax
     * @param articleId
     * @param page
     * @return
     */
    @GetMapping("/commentPage")
    @ResponseBody
    public String comment(@RequestParam(value = "articleId",required = false) Integer articleId,
                          @RequestParam(value = "page",required = false) Integer page){
        List<CommentAndUser> fiveComment = commentEntityService.getFiveComment(articleId, page);
        return JSON.toJSONString(fiveComment);
    }

    /**
     * 获取某一评论的所有评论
     * @param articleId
     * @param commentId
     * @return
     */
    @PostMapping("/getComment")
    @ResponseBody
    public String getComment(@RequestParam(value = "articleId",required = false) Integer articleId,
                             @RequestParam(value = "commentId",required = false) Integer commentId){
        List<CommentAndUser> commentAll = commentEntityService.getCommentAll(articleId, commentId);
        return JSON.toJSONString(commentAll);
    }

    /**
     * 添加评论
     * @param articleId
     * @param commentId
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/addComment")
    @ResponseBody
    public String addComment(@RequestParam(value = "articleId",required = false) Integer articleId,
                             @RequestParam(value = "commentId",required = false) Integer commentId,
                             @RequestParam(value = "comment",required = false) String comment,
                             HttpServletRequest request){
        String userName = cookieService.getUserName(request);
        if (userName.equals("")) return "未登录请登录后再评论";
        comment.replaceAll(" ", "");
        if (comment.length() == 0) return "评论内容不能为空";
        commentEntityService.addComment(articleId, commentId, comment, request);
        userCommentLogEntityService.articleComment(articleId, 1);
        return "评论成功";
    }

    /**
     * 根据id删除评论
     * @param commentId
     * @return
     */
    @GetMapping("/deleteComment")
    @ResponseBody
    public String deleteComment(@RequestParam(value = "commentId",required = false) Integer commentId,
                                @RequestParam(value = "articleId",required = false) Integer articleId){
        commentMapper.deleteById(commentId);
        userCommentLogEntityService.articleComment(articleId, -1);
        return "删除成功";
    }
}








