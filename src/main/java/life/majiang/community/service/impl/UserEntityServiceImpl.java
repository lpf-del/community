package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public UserEntity register(String username, String password, String telephone, String email) throws Exception {
        Integer nouser = this.baseMapper.exitUserName(username);
        if (nouser > 0) {
            throw new Exception("用户名已存在");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setPassWord(DigestUtils.md5DigestAsHex(password.getBytes()));
        userEntity.setTelephone(telephone);
        userEntity.setUserName(username);
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setRegisterTime(System.currentTimeMillis());
        userEntity.setMail(email);
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
        Object o = redisUtil.get(telephone);
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
        Object o = redisUtil.get(telephone);
        if (o == null){
            redisUtil.set(telephone, 1);
            redisUtil.expire(telephone, 60 * 10L);
        }
        redisUtil.incr(telephone, 1);
    }

    @Resource
    private CookieService cookieService;

    @Override
    public Model getHomePageInformation(HttpServletRequest request, Model model) {
        UserEntity userEntity = cookieService.getPersonInformation(request);
        HashMap<String, Object> map = new HashMap<>();
        map.put("likeCount", userEntity.getLikeCount());
        map.put("postCount", userEntity.getPostCount());
        map.put("heatNumber", userEntity.getHeatNumber());
        map.put("registerTime", userEntity.getRegisterTime());
        model.addAllAttributes(map);

        return model;
    }



    @Override
    public UserEntity addUserEntityByMail(String email) throws Exception {
        UserEntity userEntity = userEntityMapper.getUserEntityByEmail(email);
        if (userEntity == null){
            throw new Exception("邮箱未注册");
        }else {
            redisUtil.set(email, JSON.toJSONString(userEntity));
            return userEntity;
        }
    }

    @Override
    public void loginPhoneCode(String memPhone, String phoneCode) throws Exception {
        Object o = redisUtil.get(memPhone + "_code");
        if (o == null){
            throw new Exception("验证码失效");
        }else {
            if (!phoneCode.equals(o.toString())){
                throw new Exception("验证码输入错误");
            }
        }
    }
}
