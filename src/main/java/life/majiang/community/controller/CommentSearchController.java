package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import life.majiang.community.lucene.CommentEntitySearchIndex;
import life.majiang.community.lucene.luceneEntity.CommentLucene;
import life.majiang.community.lucene.luceneEntity.CommentPlusEntity;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author lpf
 * @description 评论的索引查询
 * @date 2022/1/6
 */
@Controller
public class CommentSearchController {

    @Resource
    private CommentEntitySearchIndex commentEntitySearchIndex;

    @GetMapping("/commentSearchIndex")
    @ResponseBody
    public String commentSearchIndex(@RequestParam(value = "str", required = false) String str,
                                     @RequestParam(value = "time", required = false) Integer time,
                                     @RequestParam(value = "articleId", required = false) Long articleId) throws IOException, ParseException {
        Long timeSize = getTimeSize(time);
        List<CommentPlusEntity> commentLuceneList = commentEntitySearchIndex.CombinationSearchIndex(str, timeSize, articleId, 20);
        return JSON.toJSONString(commentLuceneList);
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
        return 0L;
    }
}
