package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.lucene.LuenceInterface.CreateIndexLucene;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.mapper.CommentEntityMapper;
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
 * @description 评论添加索引
 * @date 2022/1/3
 */
@Component
public class CommentEntityCreateIndex extends CreateIndexLucene {

    @Resource
    private CommentEntityMapper commentEntityMapper;

    @Resource
    private DocumentEntity documentEntity;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private ArticleEntityMapper articleEntityMapper;

    /**
     * 从数据库中获取所有评论信息加入索引库
     *
     * @throws IOException
     */
    @Override
    public void createIndexAll() throws IOException {
        List<CommentEntity> comlist = commentEntityMapper.getAllDate();
        List<Document> docList = new ArrayList<>();
        for (CommentEntity commentEntity : comlist) {
            ArticleEntity articleEntity = articleEntityMapper.selectById(commentEntity.getArticleId());
            UserEntity userEntity = userEntityMapper.selectById(commentEntity.getCommentator());
            Document doc = documentEntity.getDocumentCommentEntity(commentEntity, userEntity.getUserName(), articleEntity.getTitle());
            docList.add(doc);
        }
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/commentEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
    }

    /**
     * 添加一条索引
     * @param commentEntity
     * @throws IOException
     */
    public void addIndex(CommentEntity commentEntity, String username, String title) throws IOException {
        Document doc = documentEntity.getDocumentCommentEntity(commentEntity, username, title);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/commentEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.addDocument(doc);
        indexWriter.close();
    }


}
