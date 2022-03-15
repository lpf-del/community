package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.lucene.LuenceInterface.CreateIndexLucene;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.ArticleRankingEntityMapper;
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
 * @description 文章对象创建索引
 * @date 2022/1/3
 */
@Component
public class ArticleEntityCreateIndex extends CreateIndexLucene {

    @Resource
    private ArticleEntityMapper articleEntityMapper;

    @Resource
    private DocumentEntity documentEntity;

    /**
     * 创建文章的所有索引
     * @throws IOException
     */
    @Override
    public void createIndexAll() throws IOException {
        List<ArticleEntity> comlist = articleEntityMapper.getAllDate();
        List<Document> docList = new ArrayList<>();
        for (ArticleEntity articleEntity : comlist) {
            Document doc = documentEntity.getDocumentArticleEntity(articleEntity);
            docList.add(doc);
        }
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/articleEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }



    /**
     * 创建文章的单个索引
     * @param articleEntity
     * @throws IOException
     */
    public void addIndex(ArticleEntity articleEntity) throws IOException {
        Document doc = documentEntity.getDocumentArticleEntity(articleEntity);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/articleEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.addDocument(doc);
        indexWriter.close();
    }
}
