package life.majiang.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.collections.MappingChange;
import com.zhenzi.sms.ZhenziSmsClient;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Random;

/**
 * CSDN网址
 * https://blog.csdn.net/qq_35500685/article/details/92796831
 * 榛子云短信平台用户中心
 * http://sms_developer.zhenzikj.com/zhenzisms_user/index.html
 * @author lpf
 * @description 手机验证码登录控制层
 * @date 2021/9/15
 */
@Controller
public class CodeController {
    //短信平台相关参数
    //这个不用改
    private String apiUrl = "http://sms_developer.zhenzikj.com";
    //榛子云系统上获取
    private  String appId = "110009";
    private  String appSecret = "132a87a8-0852-49b2-b972-9db2aa5efd67";
    @Resource
    private RedisUtil redisUtil;

    /**
     * 用榛子云的接口发送手机短信
     * @param memPhone
     * @param httpSession
     * @return
     */
    @ResponseBody
    @GetMapping("/fitness/code")
    public boolean getCode(@RequestParam("memPhone") String memPhone, HttpSession httpSession){
        try {
            JSONObject json = null;
            //随机生成验证码
            String code = String.valueOf(new Random().nextInt(999999));
            //将验证码通过榛子云接口发送至手机
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);

            HashMap<String, Object> map = new HashMap<>();
            map.put("templateId", "6783");
            map.put("message", "亲爱的用户，您的短信验证码为" + code + ",5分钟内有效，若非本人操作请忽略。");
            // 接收短信的手机号码
            map.put("number", memPhone);

            String result = client.send(map);

            json = JSONObject.parseObject(result);
            if (json.getIntValue("code")!=0){//发送短信失败
                return  false;
            }
            redisUtil.set(memPhone + "_code", code);
            redisUtil.expire(memPhone + "_code", 60 * 5L);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Resource
    private UserEntityService userEntityService;

    /**
     * 手机验证码登录的请求
     * @param memPhone
     * @param phoneCode
     * @param model
     * @return
     */
    @PostMapping("/loginPhoneYan")
    public String login(@RequestParam(value = "memPhone",required = false) String memPhone,
                        @RequestParam(value = "phoneCode",required = false) String phoneCode,
                        Model model){
        try {
            userEntityService.loginPhoneCode(memPhone, phoneCode);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("st3", 3);
            model.addAttribute("memPhone", memPhone);
            return "login";
        }
        return "redirect:/";
    }

}
