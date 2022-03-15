package life.majiang.community.lucene;

import life.majiang.community.lucene.LuenceInterface.SearchIndexLuence;
import life.majiang.community.lucene.luceneEntity.CommentLucene;
import life.majiang.community.lucene.luceneEntity.CommentPlusEntity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpf
 * @description 评论索引搜索
 * @date 2022/1/3
 */
@Component
public class CommentEntitySearchIndex extends SearchIndexLuence {

    /**
     * 评论搜索
     *  标准搜索：只根据评论内容进行搜索
     *  加强搜索：根据时间范围、文章id（指定文章）进行搜索
     * @param str
     * @param time
     * @param articleId
     * @param count
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<CommentPlusEntity> CombinationSearchIndex(String str, Long time, Long articleId, Integer count) throws IOException, ParseException {
        Analyzer analyzer = new IKAnalyzer();
        Query parse1 = null;
        if (time != 0) parse1 = LongPoint.newRangeQuery("commentTime", System.currentTimeMillis() - time, System.currentTimeMillis());
        QueryParser queryParser = new QueryParser("commentContent", analyzer);
        Query parse2 = queryParser.parse("commentContent:" + str);
        Query parse3 = null;
        if (articleId != 0) parse3 = queryParser.parse("articleId:" + articleId);
        BooleanQuery.Builder parse = new BooleanQuery.Builder();
        if (parse1 != null) parse.add(parse1, BooleanClause.Occur.MUST);
        parse.add(parse2, BooleanClause.Occur.MUST);
        if (parse3 != null) parse.add(parse3, BooleanClause.Occur.MUST);
        Directory dir = FSDirectory.open(Paths.get("E://luence/commentEntity"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(parse.build(), count);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<CommentPlusEntity> commentPlusEntities = new ArrayList<>();
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                CommentPlusEntity commentPlusEntity = new CommentPlusEntity();
                int socID = scoreDoc.doc;
                Document doc = indexSearcher.doc(socID);
                commentPlusEntity.setCommentId(Integer.parseInt(doc.get("commentId")));
                commentPlusEntity.setCommentator(Integer.parseInt(doc.get("commentator")));
                commentPlusEntity.setArticleId(Integer.parseInt(doc.get("articleId")));
                commentPlusEntity.setCommentContent(getContent(doc.get("commentContent"), 20));
                commentPlusEntity.setCommentTime(Long.parseLong(doc.get("commentTime")));
                commentPlusEntity.setUsername(doc.get("username"));
                commentPlusEntity.setArticleTitle(getContent(doc.get("title"), 10));
                commentPlusEntity.setCommentType(Integer.parseInt(doc.get("commentType")));
                commentPlusEntities.add(commentPlusEntity);
            }
        }
        indexReader.close();
        return commentPlusEntities;
    }

    /**
     * 评论内容展示到界面
     * @param content
     * @return
     */
    private String getContent(String content, Integer size) {
        if (content.length() < size) return content;
        content = content.replaceAll("#", "");
        content = content.replaceAll("\\n", "");
        content = content.substring(0, size);
        content += ".....";
        return content;
    }
}
