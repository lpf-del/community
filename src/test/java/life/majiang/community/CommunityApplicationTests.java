package life.majiang.community;

import life.majiang.community.deo.Question;
import life.majiang.community.mapper.QuestionMapper;
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

    @Test
    void contextLoads() {
        redisUtil.set("question",new Question());
        Question question = (Question)redisUtil.get("question");
        redisUtil.del("question");
        UUID uuid = UUID.randomUUID();
        String string = uuid.toString();
    }

}
