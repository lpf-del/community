package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleAndUserAndRang;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.ArticleEntityService;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.AddRedisCache;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 新的文章类
 * @date 2021/9/26
 */
@Service
public class ArticleEntityServiceImpl extends ServiceImpl<ArticleEntityMapper, ArticleEntity> implements ArticleEntityService {

    @Resource
    private CookieService cookieService;

    @Resource
    private ArticleEntityMapper articleEntityMapper;

    @Resource
    private ArticleRankingEntityMapper articleRankingEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private AddRedisCache addRedisCache;

    @Override
    public void addArticle(String title, String description, String myTags, String articleType, String releaseForm, String fileUrl, HttpServletRequest request, Integer x) throws Exception {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        if (personInformation == null){
            throw new Exception("请登录");
        }
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(title==null?"":title);//标题
        articleEntity.setPicturePath(fileUrl==null?"":fileUrl);//封面路径
        articleEntity.setLabel(myTags==null?"":myTags);//标签
        articleEntity.setClassification(""); //暂时用不上（分类）
        articleEntity.setType(articleType);//文章类型：原创。。。
        articleEntity.setReleaseForm(releaseForm);//文章发布形式：公开。。
        articleEntity.setContent(description==null?"":description);//文章内容
        articleEntity.setAuthorId(personInformation.getId());//作者id
        articleEntity.setX(x);//0草稿箱，1发布
        articleEntity.setReleaseTime(System.currentTimeMillis());
        articleEntityMapper.insert(articleEntity);
        Integer articleId = articleEntity.getId();
        if (x == 0) return;
        ArticleRankingEntity articleRanking = new ArticleRankingEntity();
        articleRanking.setAuthorId(personInformation.getId());//作者id
        articleRanking.setArticleId(articleId);//文章id
        articleRanking.setReleaseTime(System.currentTimeMillis());//文章发布时间
        articleRanking.setVisits(0);
        articleRanking.setComment(0);
        articleRanking.setPraiseQuantity(0);
        articleRankingEntityMapper.insert(articleRanking);
        addArticleRedis(articleRanking, articleEntity);
        addRedisCache.addUserPublishArticle(cookieService.getUserId(request), articleEntity.getId());
    }

    /**
     * 向redis存放文章信息，访问排行等设置超时时间
     * @param articleRanking
     * @param articleEntity
     */
    private void addArticleRedis(ArticleRankingEntity articleRanking, ArticleEntity articleEntity) {
        Integer articleId = articleRanking.getArticleId();
        String articleIdName = "ar_" + articleId; //文章的信息
        //文章信息
        redisUtil.set(articleIdName, JSON.toJSONString(articleEntity));
        redisUtil.expire(articleIdName, 60 * 60 * 24 * 7L);
        //文章排名
        redisUtil.set("r_" + articleIdName, JSON.toJSONString(articleRanking));
        //文章访问量排行
        redisUtil.set("v_" + articleId, 0);
    }

    @Override
    public ArticleAndUserAndRang getArticleById(Integer articleId) {
        Object o = redisUtil.get("aauar_" + articleId);
        ArticleAndUserAndRang aauar = null;
        if (o == null){
            //获取文章信息
            ArticleEntity articleEntity = getArticle(articleId);
            //获取文章的排行
            ArticleRankingEntity articleRankingEntity = getArticleRanding(articleId, articleEntity.getId());
            //获取文章的作者信息
            UserEntity author = userEntityService.getAuthor(articleEntity.getAuthorId());
            //信息放入包装类
            aauar = new ArticleAndUserAndRang(author, articleEntity, articleRankingEntity);
            redisUtil.set("aauar_" + articleId, JSON.toJSONString(aauar));
        }else {
            aauar = JSON.parseObject(o.toString(), ArticleAndUserAndRang.class);
        }
        return aauar;
    }

    @Override
    public void addUserArticleCount(HttpServletRequest request) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        personInformation.setPostCount(personInformation.getPostCount() + 1);
        userEntityMapper.updateById(personInformation);
        String userName = cookieService.getUserName(request);
        redisUtil.set(userName, JSON.toJSONString(personInformation));
        redisUtil.set("u_" + personInformation.getId(), JSON.toJSONString(personInformation));
    }


    /**
     * 获取文章的排行
     * @param articleId
     * @param id
     * @return
     */
    private ArticleRankingEntity getArticleRanding(Integer articleId, Integer id) {
        Object o = redisUtil.get("r_ar_" + articleId);
        ArticleRankingEntity articleRankingEntity = null;
        if (o == null){
            articleRankingEntity = articleRankingEntityMapper.selectById(id);
            redisUtil.set("r_ar_count_" + articleId, articleRankingEntity.getComment());
            redisUtil.set("r_ar_" + articleId, JSON.toJSONString(articleRankingEntity));
        }else {
            articleRankingEntity = JSON.parseObject(o.toString(), ArticleRankingEntity.class);
        }
        return articleRankingEntity;
    }


    /**
     * 获取文章的所有内容
     * @param articleId
     * @return
     */
    private ArticleEntity getArticle(Integer articleId) {
        Object o = redisUtil.get("ar_" + articleId);
        ArticleEntity articleEntity = null;
        if (o == null){
            articleEntity = articleEntityMapper.selectById(articleId);
            redisUtil.set("ar_" + articleId, JSON.toJSONString(articleEntity));
        }else {
            articleEntity = JSON.parseObject(JSON.parse(o.toString()).toString(), ArticleEntity.class);
        }
        return articleEntity;
    }
}
