package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.lucene.UserEntitySearchIndex;
import life.majiang.community.lucene.luceneEntity.UserLucene;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lpf
 * @description 用户搜索控制层
 * @date 2022/1/6
 */
@Controller
public class UserSearchController {

    @Resource
    private UserEntitySearchIndex userEntitySearchIndex;

    /**
     * 用户搜索可以根据用户名，uuid（网站用户编号），电话号码
     * @param str
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @GetMapping("userSearchIndex")
    @ResponseBody
    public String userSearchIndex(@RequestParam(value = "str", required = false) String str) throws IOException, ParseException {
        /**
         * 判断输入的内容是否是用户名or手机号or用户唯一标识（uuid）
         * 使用正则表达式判断：
         *      当输入为11为数字是为手机，这时使用手机号码搜索
         *      使用正则表达式匹配是否类似uuid，这时使用uuid搜索
         */
        Pattern compileTel = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Pattern compileUUid = Pattern.compile("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
        Matcher Tel = compileTel.matcher(str);
        Matcher Uuid = compileUUid.matcher(str);
        String telephone = "";
        String uuid = "";
        if (Tel.matches() && str.length() == 11){
            telephone = str;
        }else if (Uuid.matches()){
            uuid = str;
        }
        List<UserLucene> userList = userEntitySearchIndex.userSearchIndex(str, uuid, telephone);
        return JSON.toJSONString(userList);
    }
}
