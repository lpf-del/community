package life.majiang.community.util;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.service.CookieService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lpf
 * @description
 * user_article_history_userId : uah_1 #用户文章预览历史 参数：userId
 * 参数2：articleId、time
 * user_publish_article_userId : upa_1 #用户文章发布 参数：userId
 * 参数2：articleId、userId
 * user_comment_notice_userId  : ucn_1 #用户评论通知 参数：userId
 * 参数2：articleId、commentId、time、userId
 * user_heart_notice_userId    : uhn_1 #用户点赞通知 参数：userId
 * 参数2：articleId、commentId、time、userId
 * @date 2022/3/11
 */
@Component
public class AddRedisCache {


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CookieService cookieService;

    @Resource
    private GetEntityContentUtil getEntityContentUtil;

    /**
     * 用户文章预览历史
     * @param httpServletRequest
     * @param articleId
     */
    public void addUserArticleHistory(HttpServletRequest httpServletRequest, Integer articleId){
        String time = "" + System.currentTimeMillis();
        Integer userId = cookieService.getUserId(httpServletRequest);
        redisTemplate.boundZSetOps("uah_" + userId).add(articleId,new Long(time).doubleValue());
    }

    /**
     * 用户文章发布
     * @param userId
     * @param articleId
     */
    public void addUserPublishArticle(Integer userId, Integer articleId){
        redisTemplate.boundSetOps("upa_" + userId).add(articleId + "");
    }

    /**
     * 用户评论通知
     * @param id
     * @param idType
     * @param articleId
     */
    public void addUserCommentNotice(Integer id, Integer idType, Integer articleId, Integer commentId){
        Integer userId = null;
        if (idType.equals(1)){
            CommentEntity commentContent = getEntityContentUtil.getCommentContent(id);
            userId = commentContent.getCommentator();
        }else if (idType.equals(0)){
            ArticleEntity articleContent = getEntityContentUtil.getArticleContent(id);
            userId = articleContent.getAuthorId();
        }
        redisTemplate.boundZSetOps("ucn_" + userId).add(commentId, articleId);
    }

    /**
     * 用户点赞通知
     * @param userId
     * @param articleId
     */
    public void addUserHeartNotice(Integer userId, Integer articleId, Integer commentId){
        String time = "" + System.currentTimeMillis();
        redisTemplate.boundListOps("uhn_" + userId).leftPush(articleId + "_" + commentId + "_" + time);
    }

    /**
     * 关注列表
     * @param userId
     * @param authorId
     */
    public String addUserFollowList(Integer userId, Integer authorId){
        Boolean member = redisTemplate.boundSetOps("ufl_" + userId).isMember(authorId);
        if (member){
            redisTemplate.boundSetOps("ufl_" + userId).remove(authorId);
            return "取消关注成功";
        }else {
            redisTemplate.boundSetOps("ufl_" + userId).add(authorId);
            return "关注成功";
        }
    }
}
