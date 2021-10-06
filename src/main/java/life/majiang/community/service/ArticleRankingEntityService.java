package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.ArticleRankingEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 文章排行
 * @date 2021/9/26
 */
public interface ArticleRankingEntityService extends IService<ArticleRankingEntity> {

    /**
     * 文章的增加访问量，增加排行
     * 将文章的排行表的访问量加一，更新缓存
     * 相应排行的量加一
     * 用户信息的访问量加一
     * @param articleId
     * @param request
     */
    void articleVisit(Integer articleId, HttpServletRequest request);
}
