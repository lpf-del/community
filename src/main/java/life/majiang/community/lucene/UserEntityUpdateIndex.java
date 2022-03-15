package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.UserEntity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author lpf
 * @description 更新用户的索引
 * @date 2022/1/3
 */
@Component
public class UserEntityUpdateIndex {
    @Resource
    private DocumentEntity documentEntity;

    /**
     * 更新用户的索引
     * @param userEntity
     * @throws IOException
     */
    public void updateIndex(UserEntity userEntity) throws IOException {
        Document doc = documentEntity.getDocumentUserEntity(userEntity);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/userEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.updateDocument(new Term("id", String.valueOf(userEntity.getId())), doc);
        indexWriter.close();
    }
}
