package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.UserPraiseQuantityLogEntity;

/**
 * @author lpf
 * @description 用户的点赞日志
 * @date 2021/10/5
 */
public interface UserPraiseQuantityLogEntityService extends IService<UserPraiseQuantityLogEntity> {

    /**
     * 点赞日志的添加
     * @param likeUserId
     * @param likeType
     * @param likeId
     * @param userId
     */
    void addPraiseQuantityLog(Integer articleId, Integer likeUserId, Integer likeType, Integer likeId, Integer userId);

    /**
     * 作者点赞数增加
     * 更新缓存
     * @param likeUserId
     * @param likeCount
     */
    void addUserPraiseQuantity(Integer likeUserId, Integer likeCount);

    /**
     * 当点赞为评论时，评论的点赞加一
     * @param likeId
     * @param likeCount
     */
    void addCommentPraiseQuantity(Integer likeId, Integer likeCount);

    /**
     * 当点赞为文章时，文章的点赞加一，更新缓存
     * @param likeId
     * @param likeCount
     */
    void addArticlePraiseQuantity(Integer likeId, Integer likeCount);
}
