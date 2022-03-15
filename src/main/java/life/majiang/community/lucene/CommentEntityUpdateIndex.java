package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
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
 * @description 评论更新索引
 * @date 2022/1/3
 */
@Component
public class CommentEntityUpdateIndex {
    @Resource
    private DocumentEntity documentEntity;

    /**
     * 更新评论的索引
     * @param commentEntity
     * @throws IOException
     */
    public void updateIndex(CommentEntity commentEntity, String username, String title) throws IOException {
        Document doc = documentEntity.getDocumentCommentEntity(commentEntity, username, title);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/commentEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.updateDocument(new Term("commentId", String.valueOf(commentEntity.getId())), doc);
        indexWriter.close();
    }
}
