package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import life.majiang.community.deo.User;
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
    public UserEntity getHomePageInformation(HttpServletRequest request, Model model) {
        return cookieService.getPersonInformation(request);
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

    /**
     * 获取作者信息
     * @param authorId
     * @return
     */
    @Override
    public UserEntity getAuthor(Integer authorId) {
        Object o = redisUtil.get("au_" + authorId);
        UserEntity author = null;
        if (o == null){
            author = userEntityMapper.selectById(authorId);
            //作者信息简化处理
            author = Author(author);
            redisUtil.set("au_" + authorId, JSON.toJSONString(author));
        }else {
            author = JSON.parseObject(o.toString(), UserEntity.class);
        }
        return author;
    }

    @Override
    public UserEntity getHomePageInformationById(String userId) {
        return userEntityMapper.selectById(userId);
    }

    /**
     * 作者信息和用户信息用的同一数据
     *
     * 作者信息的某些信息不能泄露（如：qq，微信，电话等等），所以要变为空值: " "
     * @param author
     * @return
     */
    private UserEntity Author(UserEntity author) {
        author.setWeChat("");
        author.setQq("");
        author.setMail("");
        author.setTelephone("");
        author.setAddress("");
        author.setPassWord("");
        author.setUuid("");
        return author;
    }
}
