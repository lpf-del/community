package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
import life.majiang.community.service.ArticleRankingEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 文章排行
 * @date 2021/9/26
 */
@Service
public class ArticleRankingEntityServiceImpl extends ServiceImpl<ArticleRankingEntityMapper, ArticleRankingEntity> implements ArticleRankingEntityService {
    @Override
    public void addArticleVisit() {

    }

    @Override
    public void addArticlePraiseQuantity() {

    }

    @Override
    public void subArticlePraiseQuantity() {

    }

    @Override
    public void addArticleComment() {

    }

    @Override
    public void subArticleComment() {

    }
}
