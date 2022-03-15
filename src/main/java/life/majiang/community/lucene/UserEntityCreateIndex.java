package life.majiang.community.lucene;

import life.majiang.community.entity.UserEntity;
import life.majiang.community.lucene.LuenceInterface.CreateIndexLucene;
import life.majiang.community.mapper.UserEntityMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpf
 * @description 用户创建索引
 * @date 2022/1/3
 */
@Component
public class UserEntityCreateIndex extends CreateIndexLucene {

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private DocumentEntity documentEntity;

    /**
     * 创建userEntity表的所有索引
     * @throws IOException
     */
    @Override
    public void createIndexAll() throws IOException {
        List<UserEntity> comlist = userEntityMapper.getAllDate();
        List<Document> docList = new ArrayList<>();
        for (UserEntity userEntityEntity : comlist) {
            Document doc = documentEntity.getDocumentUserEntity(userEntityEntity);
            docList.add(doc);
        }
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/userEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    /**
     * 创建userEntity的单个索引
     * @param userEntity
     * @throws IOException
     */
    public void createIndex(UserEntity userEntity) throws IOException {
        Document doc = documentEntity.getDocumentUserEntity(userEntity);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/userEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.addDocument(doc);
        indexWriter.close();
    }
}
