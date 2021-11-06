package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.UserArticleVisitLogEntity;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.mapper.UserArticleVisitLogEntityMapper;
import life.majiang.community.service.ArticleRankingEntityService;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserArticleVisitLogEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author lpf
 * @description 文章排行
 * @date 2021/9/26
 */
@Service
public class ArticleRankingEntityServiceImpl extends ServiceImpl<ArticleRankingEntityMapper, ArticleRankingEntity> implements ArticleRankingEntityService {

    @Resource
    private CookieService cookieService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserArticleVisitLogEntityService userArticleVisitLogEntityService;

    @Resource
    private ArticleRankingEntityMapper articleRankingEntityMapper;

    @Override
    public void articleVisit(Integer articleId, HttpServletRequest request) {
        if (cookieService.getUserName(request).equals("")) return;
        Object o = redisUtil.get("v_" + articleId);
        if (o == null) {
            redisUtil.set("v_" + articleId, 1);
        }else {
            redisUtil.incr("v_" + articleId,1);
        }
        Integer authorId = addVisitCount(articleId);
        userArticleVisitLogEntityService.addUserVisitCount(authorId);
    }

    /**
     * 更新排行表的缓存
     * 获取排行表，将排行的访问量加1
     * @param articleId
     */
    private Integer addVisitCount(Integer articleId) {
        Object o = redisUtil.get("r_ar_" + articleId);
        ArticleRankingEntity articleRankingEntity = null;
        if (o == null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("article_id", articleId);
            List<ArticleRankingEntity> articleRankingEntities = articleRankingEntityMapper.selectByMap(map);
            articleRankingEntity = articleRankingEntities.get(0);
        }else {
            articleRankingEntity = JSON.parseObject(o.toString(), ArticleRankingEntity.class);
        }
        articleRankingEntity.setVisits(articleRankingEntity.getVisits() + 1);
        redisUtil.set("r_ar_" + articleId, JSON.toJSONString(articleRankingEntity));
        articleRankingEntityMapper.updateById(articleRankingEntity);
        return articleRankingEntity.getAuthorId();
    }


}
