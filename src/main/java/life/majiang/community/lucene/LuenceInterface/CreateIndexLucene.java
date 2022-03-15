package life.majiang.community.lucene.LuenceInterface;

import java.io.IOException;

/**
 * @author lpf
 * @description 创建lucene索引的接口
 * @date 2022/1/3
 */
public abstract class CreateIndexLucene {
    /**
     * 创建指定表的所有索引
     */
    public void createIndexAll() throws IOException{}

    /**
     * 创建一条索引
     */
    public void createIndex(Object o) throws IOException{}
}
