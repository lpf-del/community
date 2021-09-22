package life.majiang.community.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import life.majiang.community.deo.Question;
import life.majiang.community.mapper.QuestionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lpf
 * @description 分页逻辑
 * https://blog.csdn.net/qq_28988969/article/details/78082116
 * @date 2021/9/21
 */
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class}) //异常回滚
public class PageHelperService {

    @Resource
    private QuestionMapper questionMapper;

    public List<Question> getList(int pageNum, int pageSize) throws Exception{
        //使用分页插件
        Page<Object> objects = PageHelper.startPage(pageNum, pageSize);
        //获取数据
        List<Question> questionList = questionMapper.List();
        return questionList;
    }

}
