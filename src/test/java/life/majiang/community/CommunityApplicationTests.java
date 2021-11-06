package life.majiang.community;


import com.alibaba.fastjson.JSON;
import life.majiang.community.entity.ArticleRankingEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.util.RedisUtil;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class CommunityApplicationTests {


    @Resource
    RedisUtil redisUtil;
    @Resource
    UserEntityMapper userEntityMapper;
    @Test
    void contextLoads() {
//        Object o = redisUtil.get("19862125285");
//        System.out.println(o.toString());
//        System.out.println(JSON.parseObject(o.toString(), UserEntity.class));

//        Object r_ar_31 = redisUtil.get("r_ar_31");
//        System.out.println(r_ar_31.toString());
//        System.out.println(JSON.parseObject(r_ar_31.toString(), ArticleRankingEntity.class));
        ArticleRankingEntity articleRankingEntity = new ArticleRankingEntity();
        redisUtil.set("aa",JSON.toJSONString(articleRankingEntity));
        System.out.println(redisUtil.get("aa").toString());


    }

}
