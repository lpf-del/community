package life.majiang.community.lucene.LuenceInterface;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

/**
 * @author lpf
 * @description 搜索索引的接口
 * @date 2022/1/3
 */
public abstract class SearchIndexLuence {

    /**
     * 范围搜索
     */
    public void RangIndexSearch() throws IOException {}

    /**
     *组合搜索
     */
    public void CombinationSearchIndex() throws IOException, ParseException {}

    /**
     * 文本搜索
     */
    public void textSearchIndex() throws ParseException, IOException {}
}
