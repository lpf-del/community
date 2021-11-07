package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.CommentAndUser;
import life.majiang.community.entity.CommentEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lpf
 * @description 新的评论类
 * @date 2021/9/26
 */
public interface CommentEntityService extends IService<CommentEntity> {

    /**
     * 获取某一文章的评论前5个
     * @param articleId
     * @param page
     * @return
     */
    List<CommentAndUser> getFiveComment(Integer articleId, Integer page);


    /**
     * 获取评论的评论
     * @param articleId
     * @param commentId
     * @return
     */
    List<CommentAndUser> getCommentAll(Integer articleId, Integer commentId);

    /**
     * 添加评论
     * 当commentId为0时评论文章，不为0时评论评论
     * @param articleId
     * @param commentId
     * @param comment
     * @param request
     */
    void addComment(Integer articleId, Integer commentId, String comment, HttpServletRequest request);

    /**
     * 获取文章的回复数量
     * @return
     */
    Long getCommentCount(Integer articleId);

    /**
     * 刷新指定文章的redis缓存
     * @param articleId
     */
    void refreshAllComment(Integer articleId);

}
