package life.majiang.community.util;

import life.majiang.community.entity.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lpf
 * @description
 * user_article_history_userId : uah_1 #用户文章预览历史 参数：userId
 *  * 参数2：articleId、time
 *  * user_publish_article_userId : upa_1 #用户文章发布 参数：userId
 *  * 参数2：articleId、userId
 *  * user_comment_notice_userId  : ucn_1 #用户评论通知 参数：userId
 *  * 参数2：articleId、commentId、time、userId
 *  * user_heart_notice_userId    : uhn_1 #用户点赞通知 参数：userId
 *  * 参数2：articleId、commentId、time、userId
 * @date 2022/3/11
 */
@Component
public class GetRedisContentUtil {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private GetEntityContentUtil getEntityContentUtil;

    /**
     * 文章浏览历史
     * @param userId
     * @return
     */
    public List<UserArticleHistory> getUserArticleHistory(Integer userId){
        Set uahSet = redisTemplate.boundZSetOps("uah_" + userId).reverseRangeByScore(Double.MIN_VALUE, Double.MAX_VALUE);
        List<UserArticleHistory> userArticleHistoryList = new ArrayList<>();
        for (Object uah : uahSet) {
            Integer articleId = Integer.valueOf(uah.toString());
            Long time = redisTemplate.boundZSetOps("uah_" + userId).score(articleId).longValue();
            ArticleEntity articleContent = getEntityContentUtil.getArticleContent(articleId);
            UserEntity userContent = getEntityContentUtil.getUserContent(articleContent.getAuthorId());

            UserArticleHistory userArticleHistory = new UserArticleHistory();
            userArticleHistory.setArticleId(articleId);
            userArticleHistory.setAuthorName(userContent.getUserName());
            userArticleHistory.setAuthorId(userContent.getId());
            userArticleHistory.setPicture(articleContent.getPicturePath());
            userArticleHistory.setTitle(articleContent.getTitle());
            userArticleHistory.setTime(time);

            userArticleHistoryList.add(userArticleHistory);
        }

        return userArticleHistoryList;
    }

    /**
     * 用户文章发布
     * @param userId
     * @return
     */
    public List<UserPublishArticle> userPublishArticle(Integer userId){
        Set<Object> upaSet = redisTemplate.boundSetOps("upa_" + userId).members();
        List<UserPublishArticle> userPublishArticleList = new ArrayList<>();
        for (Object upa : upaSet) {
            Integer articleId = Integer.parseInt(upa.toString());
            ArticleEntity articleContent = getEntityContentUtil.getArticleContent(articleId);
            UserEntity userContent = getEntityContentUtil.getUserContent(articleContent.getAuthorId());
            ArticleRankingEntity articleRankingContent = getEntityContentUtil.getArticleRankingContent(articleId);
            UserPublishArticle userPublishArticle = new UserPublishArticle();

            userPublishArticle.setArticleContent(articleContent.getContent());
            userPublishArticle.setArticleId(articleId);
            userPublishArticle.setArticleTime(articleContent.getReleaseTime());
            userPublishArticle.setArticleTitle(articleContent.getTitle());
            userPublishArticle.setAuthorName(userContent.getUserName());
            userPublishArticle.setComment(articleRankingContent.getComment());
            userPublishArticle.setHeadPicture(userContent.getTouXiangUrl());
            userPublishArticle.setPraiseQuantity(articleRankingContent.getPraiseQuantity());
            userPublishArticle.setPicture(articleContent.getPicturePath());
            userPublishArticle.setAuthorId(articleContent.getAuthorId());

            userPublishArticleList.add(userPublishArticle);
        }
        return userPublishArticleList;
    }

    /**
     * 用户评论通知
     * @param userId
     * @return
     */
    public List<UserCommentNotice> getUserCommentNotice(Integer userId){
        Set ucnSet = redisTemplate.boundZSetOps("ucn_" + userId).range(0, -1);
        List<UserCommentNotice> userCommentNoticeList = new ArrayList<>();
        for(Object ucn : ucnSet){
            Integer commentId = Integer.parseInt(ucn.toString());
            Integer articleId = redisTemplate.boundZSetOps("ucn_" + userId).score(commentId).intValue();
            ArticleEntity articleContent = getEntityContentUtil.getArticleContent(articleId);
            CommentEntity commentContent = getEntityContentUtil.getCommentContent(commentId);
            CommentEntity commentContentTwo = getEntityContentUtil.getCommentContent(commentContent.getReviewedByMan());
            UserEntity userContent = getEntityContentUtil.getUserContent(commentContent.getCommentator());
            Integer commentType = commentContent.getCommentType();

            UserCommentNotice userCommentNotice = new UserCommentNotice();
            userCommentNotice.setArticleId(articleId);
            if (0 == commentType) {
                userCommentNotice.setArticleTitle(articleContent.getTitle());
                userCommentNotice.setCommentTwo("");
            }
            if (1 == commentType) {
                userCommentNotice.setCommentTwo(commentContentTwo.getCommentContent());
                userCommentNotice.setArticleTitle("");
            }
            userCommentNotice.setCommentContent(commentContent.getCommentContent());
            userCommentNotice.setCommentorId(commentContent.getCommentator());
            userCommentNotice.setCommnetorName(userContent.getUserName());
            userCommentNotice.setHeadPicture(userContent.getTouXiangUrl());

            userCommentNoticeList.add(userCommentNotice);
        }
        return userCommentNoticeList;
    }

    /**
     * 用户点赞通知
     * @param userId
     * @return
     */
    public List<UserHeartNotice> getUserHeartNotice(Integer userId){
        List<String> uhnList = redisTemplate.boundListOps("uhn_" + userId).range(1, 10);
        List<UserHeartNotice> userHeartNoticeList = new ArrayList<>();
        for (String uhn : uhnList) {
            String[] s = uhn.split("_");
            Integer articleId = Integer.parseInt(s[0]);
            Integer commentId = Integer.parseInt(s[1]);
            Long time = Long.parseLong(s[2]);
            UserEntity userContent = getEntityContentUtil.getUserContent(userId);

            UserHeartNotice userHeartNotice = new UserHeartNotice();
            userHeartNotice.setArticleId(articleId);
            if (0 == commentId) {
                ArticleEntity articleContent = getEntityContentUtil.getArticleContent(articleId);
                userHeartNotice.setArticleTitle(articleContent.getTitle());
                userHeartNotice.setCommentContent("");
            }
            if (0 != commentId) {
                CommentEntity commentContent = getEntityContentUtil.getCommentContent(commentId);
                userHeartNotice.setCommentContent(commentContent.getCommentContent());
                userHeartNotice.setArticleTitle("");
            }
            userHeartNotice.setHeadPicture(userContent.getTouXiangUrl());
            userHeartNotice.setHeartId(userContent.getId());
            userHeartNotice.setHeartName(userContent.getUserName());
            userHeartNotice.setHeartTime(time);

            userHeartNoticeList.add(userHeartNotice);
        }
        return userHeartNoticeList;
    }

    /**
     * 获取关注列表
     * @param userId
     */
    public Set<Object> getUserFollowList(Integer userId){
         return redisTemplate.boundSetOps("ufl_" + userId).members();
    }

    /**
     * 删除观看历史
     * @param userId
     * @param historyAricleId
     */
    public void deleteArticleHistory(Integer userId, Integer historyAricleId) {
        redisTemplate.boundZSetOps("uah_" + userId).remove(historyAricleId);
    }
}
