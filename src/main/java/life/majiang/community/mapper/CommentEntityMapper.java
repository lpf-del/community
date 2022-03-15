package life.majiang.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import life.majiang.community.entity.CommentEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lpf
 * @description 新的评论类
 * @date 2021/9/26
 */
public interface CommentEntityMapper extends BaseMapper<CommentEntity> {

    @Select("select * from comment_entity")
    List<CommentEntity> getAllDate();
}
