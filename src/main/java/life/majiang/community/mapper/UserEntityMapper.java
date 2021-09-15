package life.majiang.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import life.majiang.community.deo.Question;
import life.majiang.community.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lpf
 * @description 新user类
 * @date 2021/9/14
 */
public interface UserEntityMapper extends BaseMapper<UserEntity> {

    @Select("select count(1) from user_entity where user_name = #{user_name}")
    Integer exitUserName(@Param(value = "user_name") String username);

    @Select("select * from user_entity where telephone = #{telephone}")
    UserEntity exitTelephone(@Param(value = "telephone") String telephone);
}
