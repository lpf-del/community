package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.UserEntity;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;

/**
 * @author lpf
 * @description 新user类
 * @date 2021/9/14
 */
public interface UserEntityService extends IService<UserEntity> {
    /**
     * 注册
     *      向数据库中添加用户
     *      向redis添加key：电话  value：userEntity
     * @param username
     * @param password
     * @param telephone
     * @return
     * @throws Exception
     */
    UserEntity register(String username, String password, String telephone) throws Exception;

    /**
     * 查询redis是否有该电话号码 密码是否正确
     * redis没有查询mysql 两者都没有视为没有
     * @param password
     * @param telephone
     * @return
     * @throws Exception
     */
    UserEntity telephoneLogin(String password, String telephone) throws Exception;

    /**
     * 从cookie中获取电话信息
     * 从redis获取user信息
     *
     * @param cookies
     * @return
     */
    Model getHomePageInformation(Cookie[] cookies, Model model);
}
