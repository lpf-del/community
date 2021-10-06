package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.entity.UserCommentLogEntity;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.mapper.CommentEntityMapper;
import life.majiang.community.mapper.UserCommentLogEntityMapper;
import life.majiang.community.service.UserCommentLogEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author lpf
 * @description 用户的访问日志
 * @date 2021/10/5
 */
@Service
public class UserCommentLogEntityServiceImpl extends ServiceImpl<UserCommentLogEntityMapper, UserCommentLogEntity> implements UserCommentLogEntityService {

    @Resource
    private ArticleRankingEntityMapper articleRankingEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CommentEntityMapper commentEntityMapper;

    @Override
    public void articleComment(Integer articleId, Integer count) {
        Object o = redisUtil.get("r_ar_" + articleId);
        ArticleRankingEntity articleRankingEntity = null;
        if (o == null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("article_id", articleId);
            List<ArticleRankingEntity> articleRankingEntities =
                    articleRankingEntityMapper.selectByMap(map);
            articleRankingEntity = articleRankingEntities.get(0);
        }else {
            articleRankingEntity = JSON.parseObject(o.toString(), ArticleRankingEntity.class);
        }
        articleRankingEntity.setPraiseQuantity(articleRankingEntity.getPraiseQuantity() + count);
        redisUtil.set("r_ar_" + articleId, articleRankingEntity);
    }

}
