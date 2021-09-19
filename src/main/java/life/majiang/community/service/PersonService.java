package life.majiang.community.service;

import life.majiang.community.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lpf
 * @description 个人主页服务层
 * @date 2021/9/18
 */
@Service
public class PersonService {
    @Resource
    UserEntityService userEntityService;

    @Resource
    CookieService cookieService;


    /**
     * 个人信息，包括用户所有信息
     * 其中密码， 手机号， 邮箱， 微信号， qq号隐藏中间四位数
     * @param request
     * @param model
     * @return
     */
    public UserEntity getPersonInformation(HttpServletRequest request, Model model) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        personInformation.setTelephone(hideFourDigit(personInformation.getTelephone()));
        personInformation.setMail(hideFourDigit(personInformation.getMail()));
        personInformation.setQq(hideFourDigit(personInformation.getQq()));
        personInformation.setPassWord(hideFourDigit(personInformation.getPassWord()));
        personInformation.setWeChat(hideFourDigit(personInformation.getWeChat()));
        return personInformation;
    }

    /**
     * 将号码（手机号，邮箱等）隐藏中间四位
     * @param str
     * @return
     */
    public String hideFourDigit(String str){
        if (str == null) return null;
        StringBuffer sb = new StringBuffer(str);
        sb.replace(3, 7, "****");
        return sb.toString();
    }

    /**
     * 转化时间格式将Long转化成，yyyy-MM-dd的时间字符串
     * @param time
     * @return
     */
    public String timeTransformation(Long time) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.setTime(time);
        return sdf.format(date);
    }
}
