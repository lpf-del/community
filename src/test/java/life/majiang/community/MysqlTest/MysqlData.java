package life.majiang.community.MysqlTest;

import life.majiang.community.entity.CommentEntity;

import java.util.List;
public interface MysqlData {
    /**
     * 查询某一个表里的所有数据
     * @param commentEntity
     * @return
     */
    public List<CommentEntity> queryList(CommentEntity commentEntity);
}
