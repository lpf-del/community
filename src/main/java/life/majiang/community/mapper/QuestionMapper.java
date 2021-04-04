package life.majiang.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import life.majiang.community.deo.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuestionMapper extends BaseMapper<Question> {
    @Select("select * from question")
    List<Question> List();
    @Select("select * from question limit #{page},#{size}")
    List<Question> Lists(@Param(value = "page") Integer page, @Param(value = "size") Integer size);
    @Select("select count(1) from question")
    Integer count();
    @Select("select * from question where creator = #{id} limit #{page},#{size}")
    List<Question> ListsByid(@Param(value = "page") Integer page, @Param(value = "size") Integer size, @Param(value = "id") Long id);
    @Select("select count(1) from question where creator = #{id}")
    Integer countByid(@Param(value = "id") Long id);

}
