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
 * @description 文章索引的删除
 * @date 2022/1/3
 */
@Component
public class ArticleEntityDeleteIndex extends DeleteIndexLuence {

    /**
     * 删除文章所有的索引
     * @throws IOException
     */
    @Override
    public void deleteIndexAll() throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/articleEntity"));
         IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 删除文章指定索引（根据文章id）
     * @param integer
     * @throws IOException
     */
    @Override
    public void deleteIndex(Integer integer) throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/articleEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.deleteDocuments(new Term("id", String.valueOf(integer)));
        indexWriter.close();
    }
}
