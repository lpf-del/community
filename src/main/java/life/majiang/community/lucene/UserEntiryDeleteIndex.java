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
 * @description 删除用户信息的索引
 * @date 2022/1/3
 */
@Component
public class UserEntiryDeleteIndex extends DeleteIndexLuence {

    /**
     * 删除所有用户的索引
     * @throws IOException
     */
    @Override
    public void deleteIndexAll() throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/userEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 删除指定id的用户
     * @param integer
     * @throws IOException
     */
    @Override
    public void deleteIndex(Integer integer) throws IOException {
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/userEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();
        indexWriter.deleteDocuments(new Term("id", String.valueOf(integer)));
        indexWriter.close();
    }

}
