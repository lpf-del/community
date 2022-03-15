package life.majiang.community.util;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.majiang.community.entity.*;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.mapper.CommentEntityMapper;
import life.majiang.community.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lpf
 * @description 获取对象信息 （用户、文章、评论）
 * @date 2022/3/11
 */
@Component
public class GetEntityContentUtil {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CommentEntityMapper commentEntityMapper;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private ArticleEntityMapper articleEntityMapper;

    @Resource
    private ArticleRankingEntityMapper articleRankingEntityMapper;

    /**
     * 获取user对象信息
     * @param userId
     * @return
     */
    public UserEntity getUserContent(Integer userId){
        UserEntity userEntity = new UserEntity();
        Object o = redisUtil.get("u_" + userId);
        if (null == o){
            userEntity = userEntityMapper.selectById(userId);
            redisUtil.set("u_" + userId, JSON.toJSONString(userEntity));
        }else {
            userEntity = JSON.parseObject(o.toString(), UserEntity.class);
        }
        return userEntity;
    }

    /**
     * 获取comment对象信息
     * @param commentId
     * @return
     */
    public CommentEntity getCommentContent(Integer commentId){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity = commentEntityMapper.selectById(commentId);
        return commentEntity;
    }

    /**
     * 获取article对象信息
     * @param articleId
     * @return
     */
    public ArticleEntity getArticleContent(Integer articleId){
        ArticleEntity articleEntity = new ArticleEntity();
        Object o = redisUtil.get("ar_" + articleId);
        if (null == o){
            articleEntity = articleEntityMapper.selectById(articleId);
            redisUtil.set("ar_" + articleId, JSON.toJSONString(articleEntity));
        }else {
            articleEntity = JSON.parseObject(o.toString(), ArticleEntity.class);
        }
        return articleEntity;
    }

    /**
     * 获取articleranking对象信息
     * @param articleId
     * @return
     */
    public ArticleRankingEntity getArticleRankingContent(Integer articleId){
        ArticleRankingEntity articleRankingEntity;
        Object o = redisUtil.get("r_ar_" + articleId);
        if (null == o){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("article_id", articleId);
            articleRankingEntity = (ArticleRankingEntity) articleRankingEntityMapper.selectList(queryWrapper).get(0);
            redisUtil.set("r_ar_" + articleId, JSON.toJSONString(articleRankingEntity));
        }else {
            articleRankingEntity = JSON.parseObject(o.toString(), ArticleRankingEntity.class);
        }
        return articleRankingEntity;
    }
}
