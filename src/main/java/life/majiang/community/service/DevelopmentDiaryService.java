package life.majiang.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import life.majiang.community.entity.DevelopmentDiary;

import java.util.List;

/**
 * 开发日志
 */
public interface DevelopmentDiaryService extends IService<DevelopmentDiary> {
    /**
     * 根据日志id来获取开发日志信息redis->mysql
     * @param id
     * @return
     */
    DevelopmentDiary getDiaryById(int id);

    /**
     * 修改指定id的日志
     * 更新redis缓存
     * @param diaryModify
     * @param id
     */
    void setDiaryById(String diaryModify, Integer id);

    /**
     * 获取所有日志信息(分页)
     * 每请求一次十页
     * @return
     * @param page
     */
    List<DevelopmentDiary> getDiaryAll(Integer page);

    /**
     * 添加新的日志，添加缓存
     * @param diary
     * @param title
     */
    void addDiarys(String diary, String title);
}
