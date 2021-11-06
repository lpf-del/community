package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.UserEntity;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    UserEntity register(String username, String password, String telephone, String email) throws Exception;

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
     *   获取页面信息
     * @param request
     * @param model
     * @return
     */
    UserEntity getHomePageInformation(HttpServletRequest request, Model model);




    /**
     * 通过mail查询用户信息并保存redis
     * @param email
     * @return
     */
    UserEntity addUserEntityByMail(String email) throws Exception;

    /**
     * 查询redis的code看是否和输入的一致
     * @param memPhone
     * @param phoneCode
     */
    void loginPhoneCode(String memPhone, String phoneCode) throws Exception;

    /**
     * 根据作者id获取简化的user信息
     * @param authorId
     * @return
     */
    UserEntity getAuthor(Integer authorId);

    /**
     * 根据id获取个人主页的信息
     * @param userId
     */
    UserEntity getHomePageInformationById(String userId);
}