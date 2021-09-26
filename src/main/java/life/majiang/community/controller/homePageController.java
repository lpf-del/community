package life.majiang.community.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.entity.AccountInformation;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.*;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author lpf
 * @description 个人主页（重构）
 * @date 2021/9/13
 */

@Controller
public class homePageController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private PersonService personService;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private CookieService cookieService;

    @Resource
    private AccountSettingsService accountSettingsService;

    /**
     * 个人主页
     * 首先用缓存
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/homePage")
    public String homePage(HttpServletRequest request, Model model) {

        model = userEntityService.getHomePageInformation(request, model);
//        DigestUtils.md5DigestAsHex();
        return "homePage";
    }

    /**
     * 个人主页（个人资料）
     *
     * @param request
     * @param model
     * @param xiugai  为0是正常页面， 为1是修改页面
     * @return
     */
    @GetMapping("/personalInformation")
    public String personalInformation(HttpServletRequest request, Model model, Integer xiugai) {
        UserEntity userEntity = personService.getPersonInformation(request);
        model.addAttribute("userEntity", userEntity);
        if (xiugai == 1) model.addAttribute("xiugai", xiugai);
        return "personalInformation";
    }


    /**
     * 个人主页（个人资料）
     * 只允许修改用户名， 简介， 地址
     * redis缓存修改
     *
     * @param username
     * @param introduction
     * @param adress
     * @param request
     * @return
     */
    @PostMapping("/modifyInformation")
    private String telephoneLogin(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "introduction", required = false) String introduction,
                                  @RequestParam(value = "address", required = false) String adress,
                                  HttpServletRequest request) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        if (username == null || username.equals("")) username = personInformation.getUserName();
        if (introduction == null || introduction.equals("")) introduction = personInformation.getIntroduction();
        if (adress == null || adress.equals("")) adress = personInformation.getAddress();
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("user_name", username);
        updateWrapper.set("address", adress);
        updateWrapper.set("introduction", introduction);
        userEntityMapper.update(personInformation, updateWrapper);
        cookieService.RedisAndCookieUpdate(request);
        return "redirect:/personalInformation?xiugai=0";

    }

    /**
     * 账户信息页面（用来绑定和换绑，如：手机号，微信，邮箱，qq等）
     * 账户的信息安全强度
     *
     * @param request
     * @param model
     * @param status
     * @return
     */
    @GetMapping("/accountSettings")
    public String accountSettings(HttpServletRequest request, Model model, Integer status) {
        if (status == 0) {
            UserEntity userEntity = personService.getPersonInformation(request);
            AccountInformation account = accountSettingsService.accountSafetyFactor(userEntity);
            model.addAttribute("userEntity", userEntity);
            model.addAttribute("account", account);
        }
        model.addAttribute("status", status);
        return "accountSettings";
    }

    /**
     * 修改密码，查询用户信息，修改密码，更新缓存,更新cookie
     * 修改完跳到账号信息页面
     *
     * @param oldPassword
     * @param newPassword
     * @param passwordConfirmation
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/passwordModification")
    private String passwordModification(
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "passwordConfirmation", required = false) String passwordConfirmation,
            HttpServletRequest request, Model model, HttpServletResponse response) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        try {
            accountSettingsService.passwordModify(personInformation, oldPassword, newPassword, passwordConfirmation);
            cookieService.RedisAndCookieUpdate(request);
            cookieService.addUserToken(response, personInformation.getTelephone(), newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountSettings?status=1";
        }
        return "redirect:/accountSettings?status=0";
    }

    /**
     * 旧手机验证，以前的手机号+验证码，成功进入下一步，输入新的手机号
     *
     * @param oldTelephone
     * @param code
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/phoneModification")
    private String phoneModification(
            @RequestParam(value = "oldTelephone", required = false) String oldTelephone,
            @RequestParam(value = "code", required = false) String code,
            HttpServletRequest request, Model model) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        try {
            accountSettingsService.telephoneModifyCode(oldTelephone, code, personInformation);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountSettings?status=2";
        }
        return "redirect:/accountSettings?status=3";
    }

    /**
     * 更新手机号，更新缓存，cookie
     *
     * @param newTelephone
     * @param newcode
     * @param request
     * @param model
     * @param response
     * @return
     */
    @PostMapping("/newphoneModification")
    private String newphoneModification(
            @RequestParam(value = "newTelephone", required = false) String newTelephone,
            @RequestParam(value = "newcode", required = false) String newcode,
            HttpServletRequest request, Model model, HttpServletResponse response) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        try {
            accountSettingsService.updatePhone(newTelephone, newcode, personInformation, response);
            cookieService.addUserToken(response, newTelephone, newcode);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountSettings?status=3";
        }
        return "redirect:/accountSettings?status=0";
    }

    /**
     * 邮箱验证码正确，就可以进入下一步，换绑新的邮箱
     *
     * @param oldemial
     * @param oldcodeemail
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/emailModification")
    private String emailModification(
            @RequestParam(value = "oldemail", required = false) String oldemial,
            @RequestParam(value = "oldcodeemail", required = false) String oldcodeemail,
            HttpServletRequest request, Model model) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        try {
            accountSettingsService.emailModifyCode(oldemial, oldcodeemail, personInformation);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountSettings?status=5";
        }
        return "redirect:/accountSettings?status=4";
    }

    /**
     * 更新邮箱，更新redis和cookie
     *
     * @param newemial
     * @param newcodeemail
     * @param request
     * @param model
     * @param response
     * @return
     */
    @PostMapping("/newemailModification")
    private String newemailModification(
            @RequestParam(value = "newemail", required = false) String newemial,
            @RequestParam(value = "newcodeemail", required = false) String newcodeemail,
            HttpServletRequest request, Model model, HttpServletResponse response) {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        try {
            accountSettingsService.updateEamil(newemial, newcodeemail, personInformation, response);
            cookieService.addUserToken(response, newemial, newcodeemail);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountSettings?status=4";
        }
        return "redirect:/accountSettings?status=0";
    }

    /**
     * 参考文件上传
     *
     * @RequestMapping("/tupian") public String tupian(){
     * return "tupian";
     * }
     * @Resource private FileService fileService;
     * @RequestMapping(value = "/upload_action", method = RequestMethod.POST)
     * public String upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
     * <p>
     * if(file != null){
     * String upload = fileService.upload(file);
     * <p>
     * }
     * <p>
     * return "tupian";
     * <p>
     * }
     * https://blog.csdn.net/wxc1207/article/details/109521028
     */
    @Resource
    private FileService fileService;

    /**
     * 文件上传服务器，返回file_url数据库保存url
     * 用ajax异步发送用var保存返回的url
     * 上传时（文章发布的上传）携带url
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload_action", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        String upload = "";
        if (file != null) {
            upload = fileService.upload(file);
        }
        return upload;
    }
}
