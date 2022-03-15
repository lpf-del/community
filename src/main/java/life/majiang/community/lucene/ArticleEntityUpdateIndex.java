package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.lucene.LuenceInterface.UpdateIndexLuence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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
 * @description 文章索引的更新
 * @date 2022/1/3
 */
@Component
public class ArticleEntityUpdateIndex extends UpdateIndexLuence {
    @Resource
    private DocumentEntity documentEntity;

    /**
     * 更新文章的索引信息
     * @param articleEntity
     * @throws IOException
     */
    public void updateIndex(ArticleEntity articleEntity) throws IOException {
        Document doc = documentEntity.getDocumentArticleEntity(articleEntity);
        Analyzer analyzer = new IKAnalyzer();
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
//        Directory dir = FSDirectory.open(Paths.get("/opt/luence/articleEntity"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.updateDocument(new Term("id", String.valueOf(articleEntity.getId())), doc);
        indexWriter.close();
    }
}
