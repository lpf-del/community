package life.majiang.community.config;

import life.majiang.community.deo.User;
import life.majiang.community.mapper1.UserMapper1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper1 userMapper1;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String value = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie!=null&&cookie.getName().equals("token")){
                    value = cookie.getValue();
                    break;
                }
            }

            Map<String,Object> map = new HashMap();
            map.put("token",value);
            List<User> list = userMapper1.selectByMap(map);

            if (list.size()==1){
                request.getSession().setAttribute("user",list.get(0));
            }
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
