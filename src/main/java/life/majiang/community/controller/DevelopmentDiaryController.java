package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.entity.DevelopmentDiary;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.DevelopmentDiaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author lpf
 * @description 开发日志
 * @date 2021/9/28
 */
@Controller
public class DevelopmentDiaryController {

    @Resource
    private CookieService cookieService;

    @Resource
    private DevelopmentDiaryService developmentDiaryService;

    /**
     * 日志的分页展示
     * @param page
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/developmentDiary")
    public String developmentDiary(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                   HttpServletRequest request, Model model){
        Integer userLpf = cookieService.UserLpf(request);
        model.addAttribute("user_lpf", userLpf);
        List<DevelopmentDiary> list = developmentDiaryService.getDiaryAll(page);
        model.addAttribute("diary_list",list);
        return "logHomePage";
    }
    /**
     * 查看开发日志从cookie里取用户信息（username），
     * 判断用户为开发者时，可以修改开发文档，当不为开发者时，只能查看文章
     * 从数据库中获取指定id的日志信息
     * @param request
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/developmentDiaryById")
    public String developmentDiaryById(HttpServletRequest request, Model model,
                                   @RequestParam(value = "id", required = false, defaultValue = "1") Integer id){
        String username = cookieService.getUserName(request);
        if (username.equals("2568034812@qq.com") || username.equals("19862125285")){
            model.addAttribute("user_lpf", 0);
        }
        DevelopmentDiary diary = developmentDiaryService.getDiaryById(id);
        model.addAttribute("diary", diary.getDiary());
        model.addAttribute("title", diary.getTitle());
        return "development";
    }

    /**
     * 修改日志信息
     * 重定向到日志主页
     * @param diaryModify
     * @param id
     * @return
     */
    @PostMapping("/diaryModify")
    public String diaryModify(@RequestParam(value = "diaryModify", required = false) String diaryModify,
                              @RequestParam(value = "id", required = false, defaultValue = "1") Integer id){
        developmentDiaryService.setDiaryById(diaryModify, id);
        return "redirect:/developmentDiary";
    }

    /**
     * 添加新的开发日志
     * @param diary
     * @param title
     * @return
     */
    @PostMapping("/addDiary")
    public String addDiary(@RequestParam(value = "diary", required = false) String diary,
                           @RequestParam(value = "title", required = false) String title){
        developmentDiaryService.addDiarys(diary, title);
        return "redirect:/developmentDiary";
    }



}
