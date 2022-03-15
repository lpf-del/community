package life.majiang.community.lucene.LuenceInterface;

import java.io.IOException;

/**
 * @author lpf
 * @description 删除索引
 * @date 2022/1/3
 */
public abstract class DeleteIndexLuence {
    /**
     * 删除一个表中所以索引
     */
    public void deleteIndexAll() throws IOException {}
    /**
     * 删除单个索引
     */
    public void deleteIndex(Integer integer) throws IOException {}
}
