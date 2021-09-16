package life.majiang.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

    @ResponseBody
    @GetMapping("/fitness/code")
    public boolean getCode(@RequestParam("memPhone") String memPhone, HttpSession httpSession){
        try {
            JSONObject json = null;
            //随机生成验证码
            String code = String.valueOf(new Random().nextInt(999999));
            //将验证码通过榛子云接口发送至手机
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
            System.out.println(memPhone + "_" + code);
            String result = client.send(memPhone, "您的验证码为:" + code + "，该码有效期为5分钟，该码只能使用一次!");

            json = JSONObject.parseObject(result);
            System.out.println(json.getIntValue("code"));
            if (json.getIntValue("code")!=0){//发送短信失败
                return  false;
            }
            //将验证码存到session中,同时存入创建时间
            //以json存放，这里使用的是阿里的fastjson
            json = new JSONObject();
            json.put("memPhone",memPhone);
            json.put("code",code);
            json.put("createTime",System.currentTimeMillis());
            // 将认证码存入SESSION
            httpSession.setAttribute("code",json);
            redisUtil.set(memPhone + "_code", json.toJSONString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
