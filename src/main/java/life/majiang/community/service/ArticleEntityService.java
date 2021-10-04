package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.ArticleAndUserAndRang;
import life.majiang.community.entity.ArticleEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 新的文章类
 * @date 2021/9/26
 */
public interface ArticleEntityService extends IService<ArticleEntity> {

    /**
     * 文章发布， 添加数据库， 添加redis（文章id+内容）
     * @param title
     * @param description
     * @param myTags
     * @param articleType
     * @param releaseForm
     * @param fileUrl
     * @param request
     * @throws Exception
     */
    void addArticle(String title, String description, String myTags, String articleType, String releaseForm, String fileUrl, HttpServletRequest request, Integer x) throws Exception;

    /**
     * 用文章id
     * 获取文章信息
     * 获取作者的信息
     * 获取文章的排名等
     * @param articleId
     * @return
     */
    ArticleAndUserAndRang getArticleById(Integer articleId);
}
