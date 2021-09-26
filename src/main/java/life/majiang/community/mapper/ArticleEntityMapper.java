package life.majiang.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import life.majiang.community.entity.ArticleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lpf
 * @description 新的文章类
 * @date 2021/9/26
 */
public interface ArticleEntityMapper extends BaseMapper<ArticleEntity> {

    @Select("select * from article_entity where user_name = #{user_name}")
    Integer exitUserName(@Param(value = "user_name") String username);
}
