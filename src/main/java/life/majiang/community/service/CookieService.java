package life.majiang.community.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author lpf
 * @description cookie的操作
 * @date 2021/9/18
 */
@Service
public class CookieService {

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    public String getUserToken(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        String token = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        return token;
    }

    /**
     * 添加user的token
     * token是MD5_字符串 + 手机号（邮箱） + 密码（验证码）  token和密码（验证码）要md5加密
     * token放入redis 保存7天， 并将token和手机号（邮箱）保存到cookie中
     * @param response
     * @param email_phone
     * @param password_code
     */
    public void addUserToken(HttpServletResponse response, String email_phone, String password_code) {
        password_code = DigestUtils.md5DigestAsHex(password_code.getBytes());
        String token = DigestUtils.md5DigestAsHex(("MD_5" + email_phone + password_code).getBytes());
        redisUtil.set("MD5_" + email_phone, token);
        redisUtil.expire("MD5_" + email_phone, 60 * 60 * 24 * 7L);
        response.addCookie(new Cookie("token", token));
        response.addCookie(new Cookie("username", email_phone));
    }

    /**
     * 用username获取个人信息
     * username: 用手机登陆时为手机号， 用邮箱登录时是邮箱
     *     username从cookie中获取，先查redis后查mysql返回user所有信息
     * @param request
     * @return
     */
    public UserEntity getPersonInformation(HttpServletRequest request) {
        String username = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("username")) {
                username = cookie.getValue();
            }
        }
        UserEntity userEntity;
        Object o = redisUtil.get(username);
        if (o == null) {
//            userEntity = userEntityMapper.getUserEntityByUserName(username);
            if (username.contains("@")){
                userEntity = userEntityMapper.getUserEntityByEmail(username);
            }else {
                userEntity = userEntityMapper.getUserEntityByPhone(username);
            }
            redisUtil.set(username, JSON.toJSONString(userEntity));
        } else {
            userEntity = JSON.parseObject(o.toString(), UserEntity.class);
        }
        return userEntity;
    }

    /**
     * redis缓存更新
     * 从cookie取出username
     * 查询数据库，修改对应数据
     * @param request
     */
    public void RedisAndCookieUpdate(HttpServletRequest request){
        String username = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("username")){
                username = cookie.getValue();
            }
        }
        UserEntity userEntity = null;
        if (username.contains("@")){
            userEntity = userEntityMapper.getUserEntityByEmail(username);
        }else {
            userEntity = userEntityMapper.getUserEntityByPhone(username);
        }
        redisUtil.set(username, JSON.toJSONString(userEntity));
    }
}
