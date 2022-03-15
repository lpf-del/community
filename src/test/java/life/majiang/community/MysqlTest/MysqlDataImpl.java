package life.majiang.community.MysqlTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.mapper.CommentEntityMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lpf
 * @description luence数据库导入，将mysql数据库中的数据存入文件方便luence查询
 * @date 2021/12/15
 */
public class MysqlDataImpl implements MysqlData {
    @Resource
    public CommentEntityMapper commentEntityMapper;
    @Override
    public List<CommentEntity> queryList(CommentEntity commentEntity) {
        List<CommentEntity> list = commentEntityMapper.selectList(new QueryWrapper<>());
        

        return null;
    }
}
