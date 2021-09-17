package life.majiang.community.config;

import life.majiang.community.deo.User;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = "";
        String telephone = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    token = cookie.getValue();
                }else if (cookie.getName().equals("telephone")){
                    telephone = cookie.getValue();
                }
            }
            /**
             * 检查cookie的token密文是否在redis 没有就拦截 跳到登录界面
             *
             */

            String md5 = (String)redisUtil.get("MD5_" + telephone);
            if ((md5 != null && !md5.equals(token)) || md5 == null){
                request.getRequestDispatcher("/log").forward(request, response);
                return false;
            }

            User user = new User();
            user.setName("lpf");
            user.setGmtCreate(1627982267510L);
            user.setAccountId(8871126L);
            request.getSession().setAttribute("user", user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
