package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.entity.UserArticleHistory;
import life.majiang.community.entity.UserCommentNotice;
import life.majiang.community.entity.UserHeartNotice;
import life.majiang.community.entity.UserPublishArticle;
import life.majiang.community.service.CookieService;
import life.majiang.community.util.AddRedisCache;
import life.majiang.community.util.GetRedisContentUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lpf
 * @description 消息通知控制层
 * 1.关注人的动态
 * 2.点赞的消息
 * 3.评论的消息
 * 4.文章浏览历史
 * 5.关注
 * @date 2022/3/12
 */
@Controller
public class NoticeController {
    @Resource
    private GetRedisContentUtil getRedisContentUtil;

    @Resource
    private AddRedisCache addRedisCache;

    @Resource
    private CookieService cookieService;

    /**
     * 文章浏览历史
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/userArticleHistory")
    @ResponseBody
    public String userArticleHistory(HttpServletRequest httpServletRequest){
        Integer userId = cookieService.getUserId(httpServletRequest);
        List<UserArticleHistory> userArticleHistory = getRedisContentUtil.getUserArticleHistory(userId);
        return JSON.toJSONString(userArticleHistory);
    }

    @GetMapping("/deleteArticleHistory")
    @ResponseBody
    public String deleteArticleHistory(HttpServletRequest httpServletRequest, Integer historyArticleId){
        Integer userId = cookieService.getUserId(httpServletRequest);
        getRedisContentUtil.deleteArticleHistory(userId, historyArticleId);
        return "删除成功";
    }
    /**
     * 被评论的消息
     * @return
     */
    @GetMapping("/userCommentNotice")
    @ResponseBody
    public String userCommentNotice(HttpServletRequest httpServletRequest){
        Integer userId = cookieService.getUserId(httpServletRequest);
        List<UserCommentNotice> userCommentNotice = getRedisContentUtil.getUserCommentNotice(userId);
        return JSON.toJSONString(userCommentNotice);
    }

    /**
     * 被点赞的消息
     * @param userId
     * @return
     */
    @GetMapping("/userHeartNotice")
    @ResponseBody
    public String userHeartNotice(Integer userId){
        List<UserHeartNotice> userHeartNotice = getRedisContentUtil.getUserHeartNotice(userId);
        return JSON.toJSONString(userHeartNotice);
    }

    /**
     * 关注人的动态
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/userPublishArticle")
    @ResponseBody
    public String userPublishArticle(HttpServletRequest httpServletRequest){
        Integer userId = cookieService.getUserId(httpServletRequest);
        Set<Object> userFollowList = getRedisContentUtil.getUserFollowList(userId);
        List<UserPublishArticle> upaList = new ArrayList<>();
        for (Object authorId : userFollowList) {
            if (userId.equals(authorId)) continue;
            List<UserPublishArticle> userPublishArticleList = getRedisContentUtil.userPublishArticle(Integer.parseInt(authorId.toString()));
            userPublishArticleList.stream().collect(Collectors.toCollection(() -> upaList)).sort(new Comparator<UserPublishArticle>() {
                @Override
                public int compare(UserPublishArticle upa1, UserPublishArticle upa2) {
                    return new Long(upa2.getArticleTime() - upa1.getArticleTime()).intValue();
                }
            });
        }
        return JSON.toJSONString(upaList);
    }

    /**
     * 关注（关注、取消）
     * @param httpServletRequest
     * @param authorId
     * @return
     */
    @GetMapping("/addFollow")
    @ResponseBody
    public String addFollow(HttpServletRequest httpServletRequest, Integer authorId){
        Integer userId = cookieService.getUserId(httpServletRequest);
        if (userId.equals(authorId)) return "不可以关注自己";
        return addRedisCache.addUserFollowList(userId, authorId);
    }
}