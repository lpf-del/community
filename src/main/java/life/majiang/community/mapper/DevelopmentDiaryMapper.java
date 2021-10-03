package life.majiang.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.DevelopmentDiary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 开发日志
 * @date 2021/9/28
 */
public interface DevelopmentDiaryMapper extends BaseMapper<DevelopmentDiary> {
    @Select("select * from development_diary where id = #{id}")
    String getDiaryById(@Param(value = "id") Integer id);


}