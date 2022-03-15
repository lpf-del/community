package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.lucene.ArticleEntitySearchIndex;
import life.majiang.community.lucene.luceneEntity.ArticleLucene;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author lpf
 * @description 搜索的控制层
 * @date 2021/11/7
 */
@Controller
public class ArticleSearchController {

    @Resource
    private ArticleEntitySearchIndex articleEntitySearchIndex;

    /**
     * 跳转到搜索页面
     *
     * @return
     */
    @GetMapping("/toSearch")
    public String toSearch() {
        return "search";
    }

    @GetMapping("/articleLuceneSearch")
    @ResponseBody
    public String luceneSearch(@RequestParam(value = "str", required = false) String str,
                               @RequestParam(value = "time", required = false) Integer time,
                               @RequestParam(value = "label", required = false) String label) throws IOException, ParseException {
        Long timeSize = getTimeSize(time);
        List<ArticleLucene> articleLucenes = articleEntitySearchIndex.articleSearchIndex(str, label, timeSize, 10);
        return JSON.toJSONString(articleLucenes);
    }

    //一天时间多少毫秒
    private final Long timeDay = 1000 * 60 * 60 * 24L;

    /**
     * 根据time值获取多少毫秒
     *
     * @param time
     * @return
     */
    private Long getTimeSize(Integer time) {
        if (time == 1) return timeDay; //一天
        else if (time == 2) return timeDay * 7; //一周
        else if (time == 3) return timeDay * 30; //一月
        else if (time == 4) return timeDay * 365; //一年
        return null;
    }
}
