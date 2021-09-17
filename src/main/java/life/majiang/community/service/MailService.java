package life.majiang.community.service;

import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * @author lpf
 * @description mail登录接口
 * https://blog.csdn.net/weixin_44421461/article/details/111055930
 * @date 2021/9/17
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    //application.properties中已配置的值
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 给前端输入的邮箱，发送验证码
     * @param email
     * @return
     */
    public boolean sendMimeMail(String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("验证码邮件");//主题
            //生成随机数
            String code = randomCode();


            //将随机数放置到redis中
            String mail_code = email + "_code";
            redisUtil.set(mail_code, code);

            mailMessage.setText("（lpf社区：）您收到的验证码是：" + code + "五分钟后验证码失效");//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送

            redisUtil.expire(mail_code, 60 * 5L);//给邮箱的验证码设置超值时长
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 随机生成6位数的验证码
     * @return String code
     */
    public String randomCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

//    /**
//     * 检验验证码是否一致
//     * @param userVo
//     * @param session
//     * @return
//     */
//    public boolean registered(UserVo userVo, HttpSession session){
//        //获取session中的验证信息
//        String email = (String) session.getAttribute("email");
//        String code = (String) session.getAttribute("code");
//
//        //获取表单中的提交的验证信息
//        String voCode = userVo.getCode();
//
//        //如果email数据为空，或者不一致，注册失败
//        if (email == null || email.isEmpty()){
//            //return "error,请重新注册";
//            return false;
//        }else if (!code.equals(voCode)){
//            //return "error,请重新注册";
//            return false;
//        }
//
//        //保存数据
//        User user = UserVoToUser.toUser(userVo);
//
//        //将数据写入数据库
//        userMapper.insertUser(user);
//
//        //跳转成功页面
//        return true;
//    }

    /**
     * 先通过email查询是否有这个人（先redis， 后mysql）
     * 通过输入email和 code 查询redis.get(mail_code)是否存在
     * @param email
     * @param code
     * @return
     */

    public boolean loginIn(String email, String code) throws Exception {
        Object emailUser = redisUtil.get(email);
        if (emailUser == null){
            UserEntity userEntity = userEntityService.addUserEntityByMail(email);
        }
        String mail_code = email + "_code";
        Object m_c = redisUtil.get(mail_code);
        if (!m_c.toString().equals(code)){
            throw new Exception("验证码不正确，请重新输入验证码");
        }
        return true;
    }
}