package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.service.ArticleEntityService;
import life.majiang.community.service.CookieService;
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
        articleEntity.setAuthorId(personInformation.getUserId());//作者id
        articleEntity.setX(x);//0草稿箱，1发布
        articleEntityMapper.insert(articleEntity);
        Integer articleId = articleEntity.getId();
        if (x == 0) return;
        ArticleRankingEntity articleRanking = new ArticleRankingEntity();
        articleRanking.setAuthorId(personInformation.getUserId());//作者id
        articleRanking.setArticleId(articleId);//文章id
        articleRanking.setReleaseTime(System.currentTimeMillis());//文章发布时间
        articleRankingEntityMapper.insert(articleRanking);
        addArticleRedis(articleRanking, articleEntity);
    }

    private void addArticleRedis(ArticleRankingEntity articleRanking, ArticleEntity articleEntity) {
        Integer articleId = articleRanking.getArticleId();
        String articleIdName = "ar_" + articleId; //文章的信息
        //文章信息
        redisUtil.set(articleIdName, JSON.toJSONString(articleEntity));
        redisUtil.expire(articleIdName, 60 * 60 * 24 * 7L);
        //文章访问量排行
        redisUtil.set("v_" + articleId, 0);
    }
}
