package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author lpf
 * @description 新user类
 * @date 2021/9/14
 */
@Service
public class UserEntityServiceImpl extends ServiceImpl<UserEntityMapper, UserEntity> implements UserEntityService {
    @Override
    public UserEntity register(String username, String password, String telephone) throws Exception {
        Integer nouser = this.baseMapper.exitUserName(username);
        if (nouser > 0) {
            throw new Exception("用户名已存在");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setLikeCount(0);
        userEntity.setPostCount(0);
        userEntity.setHeatNumber(0);
        userEntity.setAddress("");
        userEntity.setPassWord(DigestUtils.md5DigestAsHex(password.getBytes()));
        userEntity.setTelephone(telephone);
        userEntity.setUserName(username);
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setRegisterTime(System.currentTimeMillis());

        this.baseMapper.insert(userEntity);
        return userEntity;
    }

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Override
    public UserEntity telephoneLogin(String password, String telephone) throws Exception {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //3次密码错误不能登录
        Object o = redisUtil.get(telephone + "_" + telephone);
        if(o != null && (o.toString()).equals("3")){
            throw new Exception("请十分钟后再输入");
        }

        UserEntity userEntity = null;
        userEntity = JSON.parseObject((String)redisUtil.get(telephone), UserEntity.class);
        if (userEntity == null) {
            userEntity = userEntityMapper.exitTelephone(telephone);
            if (userEntity != null) {
                redisUtil.set(telephone, JSON.toJSONString(userEntity));
            } else {
                throw new Exception("电话不存在");
            }
        }
        if (!userEntity.getPassWord().equals(password)){
            repeatErrorLogin(telephone);
            throw new Exception("密码错误");
        }
        return userEntity;
    }

    /**
     * 同一个电话多次密码输入错误
     * 记录错误次数
     * @param telephone
     */
    private void repeatErrorLogin(String telephone) {
        Object o = redisUtil.get(telephone + "_" + telephone);
        if (o == null){
            redisUtil.set(telephone + "_" + telephone, 0);
            redisUtil.expire(telephone + "_" + telephone, 60 * 10L);
        }
        redisUtil.incr(telephone + "_" + telephone, 1);
    }

    @Override
    public Model getHomePageInformation(Cookie[] cookies, Model model) {
        String telephone = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("telephone")){
                telephone = cookie.getValue();
            }
        }

        UserEntity userEntity = JSON.parseObject((String) redisUtil.get(telephone), UserEntity.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("likeCount", userEntity.getLikeCount());
        map.put("postCount", userEntity.getPostCount());
        map.put("heatNumber", userEntity.getHeatNumber());
        map.put("registerTime", userEntity.getRegisterTime());
        model.addAllAttributes(map);

        return model;
    }
}
