package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.DevelopmentDiary;
import life.majiang.community.mapper.DevelopmentDiaryMapper;
import life.majiang.community.service.DevelopmentDiaryService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lpf
 * @description 开发日志
 * @date 2021/9/28
 */
@Service
public class DevelopmentDiaryServiceImpl extends ServiceImpl<DevelopmentDiaryMapper, DevelopmentDiary> implements DevelopmentDiaryService {
    @Resource
    private DevelopmentDiaryMapper developmentDiaryMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public DevelopmentDiary getDiaryById(int id) {
        Object o = redisUtil.get("diary_" + id);
        if (o == null){
            String diary = developmentDiaryMapper.getDiaryById(id);
            redisUtil.set("diary_" + id, JSON.toJSONString(diary));
        }
        DevelopmentDiary developmentDiary = JSON.parseObject(o.toString(), DevelopmentDiary.class);
        return developmentDiary;
    }

    @Override
    public void setDiaryById(String diaryModify, Integer id) {
        DevelopmentDiary developmentDiary = new DevelopmentDiary();
        developmentDiary.setId(id);
        developmentDiary.setDiary(diaryModify);
        developmentDiary.setDiaryTime(System.currentTimeMillis());
        developmentDiaryMapper.updateById(developmentDiary);

        DevelopmentDiary diary = getDiaryById(id);
        redisUtil.set("diary_" + id, JSON.toJSONString(diary));
    }

    @Override
    public List<DevelopmentDiary> getDiaryAll(Integer page) {
        Object o = redisUtil.get("diary_page_" + page);
        if (o == null){
            Integer integer = developmentDiaryMapper.selectCount(null);
            int pageMax = integer/10 + integer%10!=0?1:0;
            System.out.println(pageMax);
            Page<DevelopmentDiary> developmentDiaryPage =
                    developmentDiaryMapper.selectPage(new Page<DevelopmentDiary>(Math.min(page,pageMax), 10), new QueryWrapper<DevelopmentDiary>().orderByDesc("diary_time"));
            List<DevelopmentDiary> records = developmentDiaryPage.getRecords();
            redisUtil.set("diary_page_" + page, JSON.toJSONString(records));
            return records;
        }
        List<DevelopmentDiary> records = (List<DevelopmentDiary>) JSON.parseObject(o.toString(), List.class);
        return records;
    }

    @Override
    public void addDiarys(String diary, String title) {
        DevelopmentDiary addDiary = new DevelopmentDiary();
        addDiary.setDiaryTime(System.currentTimeMillis());
        addDiary.setDiary(diary==null?"":diary);
        addDiary.setTitle(title==null?"":title);
        developmentDiaryMapper.insert(addDiary);
        Integer id = addDiary.getId();

        redisUtil.set("diary_" + id, JSON.toJSONString(addDiary));
    }
}
