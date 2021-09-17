package life.majiang.community.controller;

import life.majiang.community.entity.UserEntity;
import life.majiang.community.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author lpf
 * @description mail的控制层
 * @date 2021/9/17
 */
@Controller
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(String email, Model model){
        mailService.sendMimeMail(email);
    }

//    @PostMapping("/regist")
//    @ResponseBody
//    public String regist(UserVo userVo, HttpSession session){
//        UserEntity userEntity = mailService.registered(userVo,session);
//        return "sucess";
//    }

    /**
     * 邮箱+验证码
     * @param email
     * @param code
     * @return
     */
    @PostMapping("/loginYan")
    @ResponseBody
    public String login(String email, String code, Model model){
        try {
            mailService.loginIn(email, code);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("st2", 2);
            model.addAttribute("email", email);
            return "login";
        }
        return "redirect:/";
    }

}
