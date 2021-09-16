package life.majiang.community;

import life.majiang.community.deo.Question;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
class CommunityApplicationTests {


    @Resource
    RedisUtil redisUtil;
    @Resource
    UserEntityMapper userEntityMapper;
    @Test
    void contextLoads() {
        System.out.println((String) null);
    }

}
