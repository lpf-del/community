package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.UserArticleVisitLogEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 用户的访问日志
 * @date 2021/10/5
 */
public interface UserArticleVisitLogEntityService extends IService<UserArticleVisitLogEntity> {
    /**
     * 添加用户访问日志
     * @param articleId
     * @param request
     */
    void addVisitLog(Integer articleId, HttpServletRequest request);

    /**
     * 访问量+1
     * 更新用户的访问日志
     * 将文章的排行，文章的访问量，作者的访问量三者加一
     * @param articleId
     * @param request
     */
    void addArticleVisit(Integer articleId, HttpServletRequest request);

    /**
     * 将用户的访问量字段加一
     * @param articleId
     */
    void addUserVisitCount(Integer articleId);
}
