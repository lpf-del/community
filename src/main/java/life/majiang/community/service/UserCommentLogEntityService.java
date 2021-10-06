package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.UserCommentLogEntity;

/**
 * @author lpf
 * @description 用户的评论日志
 * @date 2021/10/5
 */
public interface UserCommentLogEntityService extends IService<UserCommentLogEntity> {

    /**
     * 文章评论数量增加
     * 根据文章id，改变评论字段数量
     */
    void articleComment(Integer articleId, Integer count);

}
