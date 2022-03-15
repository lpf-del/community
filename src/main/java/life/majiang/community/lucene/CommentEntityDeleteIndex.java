package life.majiang.community.lucene;

import life.majiang.community.lucene.LuenceInterface.DeleteIndexLuence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author lpf
 * @description 评论删除索引
 * @date 2022/1/3
 */
@Component
public class CommentEntityDeleteIndex extends DeleteIndexLuence {

    /**
     * 删除评论的所有索引
     * @throws IOException
     */
    @Override
    public void deleteIndexAll() throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/commentEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.close();

    }

    /**
     * 删除指定评论的索引（根据id）
     * @param integer
     * @throws IOException
     */
    @Override
    public void deleteIndex(Integer integer) throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/commentEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.deleteDocuments(new Term("commentId", String.valueOf(integer)));
        indexWriter.close();
    }


}
