package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.entity.UserPraiseQuantityLogEntity;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.mapper.CommentEntityMapper;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.mapper.UserPraiseQuantityLogEntityMapper;
import life.majiang.community.service.UserPraiseQuantityLogEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lpf
 * @description 用户的点赞日志
 * @date 2021/10/5
 */
@Service
public class UserPraiseQuantityLogEntityServiceImpl extends ServiceImpl<UserPraiseQuantityLogEntityMapper, UserPraiseQuantityLogEntity> implements UserPraiseQuantityLogEntityService {
    @Resource
    private UserPraiseQuantityLogEntityMapper userPraiseQuantityLogEntityMapper;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CommentEntityMapper commentEntityMapper;

    @Resource
    private ArticleRankingEntityMapper articleRankingEntityMapper;

    @Override
    public void addPraiseQuantityLog(Integer articleId, Integer likeUserId, Integer likeType, Integer likeId, Integer userId) {
        UserPraiseQuantityLogEntity upql = new UserPraiseQuantityLogEntity();
        upql.setAuthorId(likeUserId);//作者的id
        upql.setPraiseQuantityTime(System.currentTimeMillis());//点赞时间
        upql.setUserId(userId);//点赞的用户id
        upql.setPraiseQuantityType(likeType);//点赞的类型
        upql.setPraiseQuantityId(likeId);//被点赞的id
        userPraiseQuantityLogEntityMapper.insert(upql);
    }

    @Override
    public void addUserPraiseQuantity(Integer likeUserId, Integer likeCount) {
        UserEntity userEntity = userEntityMapper.selectById(likeUserId);
        userEntity.setLikeCount(userEntity.getLikeCount() + likeCount);
        userEntityMapper.updateById(userEntity);
        redisUtil.set(userEntity.getMail(), JSON.toJSONString(userEntity));
        redisUtil.set(userEntity.getTelephone(), JSON.toJSONString(userEntity));
    }

    @Override
    public void addCommentPraiseQuantity(Integer likeId, Integer likeCount) {
        CommentEntity commentEntity = commentEntityMapper.selectById(likeId);
        commentEntity.setLikeCount(commentEntity.getLikeCount() + likeCount);
        commentEntityMapper.updateById(commentEntity);
    }

    @Override
    public void addArticlePraiseQuantity(Integer likeId, Integer likeCount) {
        ArticleRankingEntity articleRankingEntity = articleRankingEntityMapper.selectById(likeId);
        Integer articleId = articleRankingEntity.getArticleId();
        articleRankingEntity.setPraiseQuantity(articleRankingEntity.getPraiseQuantity() + likeCount);
        redisUtil.set("r_ar_" + articleId, JSON.toJSONString(articleRankingEntity));
    }
}
