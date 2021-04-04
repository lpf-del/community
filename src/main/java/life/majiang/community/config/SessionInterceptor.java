package life.majiang.community.config;

import life.majiang.community.deo.User;
import life.majiang.community.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@AllArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final UserMapper userMapper;
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

            System.out.println(value);
            Map<String,Object> map = new HashMap();
            map.put("token",value);
            System.out.println(userMapper);
            List<User> list = userMapper.selectByMap(map);
            for (Object o : list) {
                System.out.println(o);
            }


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
