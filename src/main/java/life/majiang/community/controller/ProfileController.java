package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.deo.Ifica;
import life.majiang.community.deo.Notification;
import life.majiang.community.deo.User;
import life.majiang.community.entity.ArticleAndUserAndRang;
import life.majiang.community.entity.CommentAndUser;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ProfileController {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private QuestionDtoService questionDtoService;

    @Autowired
    @SuppressWarnings("all")
    private NotificationMapper notificationMapper;

    @GetMapping("/profileold/{action}")
    public String profileold(@PathVariable(name = "action") String action,
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
            if(action.equals("question")){
                PageDTO pageDTO = questionDtoService.Listwen(page,user.getAccountId());
                model.addAttribute("pageDTO",pageDTO);
            }else if (action.equals("repies")){
                Map<String,Object> map = new HashMap<>();
                map.put("receiver",user.getAccountId());
                List<Notification> notifications = notificationMapper.selectByMap(map);


                List<Ifica> list = new ArrayList<>();
                for (Notification notification : notifications) {
                    User user1 = userMapper1.selectById(notification.getNotifier());
                    Ifica ifica = new Ifica();
                    ifica.setS1(user1.getName());
                    Long type = notification.getType();
                    if (type == 0L || type == 1L){
                        ifica.setS2("  回复你  ");
                    }
                    ifica.setS3(notification.getIfication());
                    ifica.setId(notification.getQuestionId());
                    list.add(ifica);
                }
                model.addAttribute("unreadCounts",list);
            }
        }else {
            return "redirect:/";
        }

        return "profileold";
    }

    @Resource
    private ArticleEntityService articleEntityService;

    @Resource
    private CommentEntityService commentEntityService;

    @Resource
    private UserArticleVisitLogEntityService userArticleVisitLogEntityService;

    @Resource
    private UserPraiseQuantityLogEntityService userPraiseQuantityLogEntityService;

    @Resource
    private CookieService cookieService;


    /**
     * 查看文章的请求
     * 获取文章的所有信息：作者、文章内容、排名（文章）、评论数、点赞数等
     * 获取前十条评论（评论文章的），评论的评论之后请求再获得
     * @param articleId
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/profile")
    public String profile(@RequestParam(value = "articleId",required = false) Integer articleId,
                           Model model, HttpServletRequest request){
        ArticleAndUserAndRang aau = articleEntityService.getArticleById(articleId);
        List<CommentAndUser> cau = commentEntityService.getFiveComment(articleId, 1);
        model.addAttribute("articleAndUserAndRang", aau);
        model.addAttribute("commentList", cau);
        List<String> list = labelS(aau.getArticleEntity().getLabel());
        model.addAttribute("labelList", list);
        //userArticleVisitLogEntityService.addArticleVisit(articleId, request);
        return "profile";
    }

    /**
     * 标签的拆分：（改为前端实现）
     * @param label
     * @return
     */
    public List<String> labelS(String label){
        String[] split = label.split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }
        list.remove(0);
        return list;
    }
    /**
     * 点赞的请求
     * 文章的点赞数加一
     * 作者的点赞数加一
     * 点赞的日志
     * @param articleId  文章id
     * @param likeUserId 被点赞的用户id
     * @param likeId     被点赞的id（评论或文章）
     * @param likeType   点赞类型，标识是评论还是文章
     * @param likeCount  点赞还是取消点赞
     * @param request
     * @return
     */
    @GetMapping("/likeCount")
    @ResponseBody
    public String likeCount(@RequestParam(value = "articleId",required = false) Integer articleId,
                            @RequestParam(value = "likeUserId",required = false) Integer likeUserId,
                            @RequestParam(value = "likeId",required = false) Integer likeId,
                            @RequestParam(value = "likeType",required = false) Integer likeType,
                            @RequestParam(value = "likeCount",required = false) Integer likeCount,
                            HttpServletRequest request){
        Integer userId = cookieService.getUserId(request);
        //添加点赞日志
        userPraiseQuantityLogEntityService.addPraiseQuantityLog(articleId, likeUserId, likeType, likeId, userId);
        //作者点赞数增加
        userPraiseQuantityLogEntityService.addUserPraiseQuantity(likeUserId, likeCount);
        //文章的点赞数增加
        if (likeType == 1) userPraiseQuantityLogEntityService.addArticlePraiseQuantity(likeId, likeCount);
        if (likeType == 0) userPraiseQuantityLogEntityService.addCommentPraiseQuantity(likeId, likeCount);
        if (likeCount == -1) return "取消点赞";
        return "点赞成功";
    }
}
