package life.majiang.community.lucene;

import life.majiang.community.lucene.LuenceInterface.SearchIndexLuence;
import life.majiang.community.lucene.luceneEntity.ArticleLucene;
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
 * @description 文章的索引查询
 * @date 2022/1/3
 */
@Component
public class ArticleEntitySearchIndex extends SearchIndexLuence {

    /**
     * 文章lucene搜索方法
     * @param str
     * @param label
     * @param time
     * @param count
     * @throws IOException
     * @throws ParseException
     */
    public List<ArticleLucene> articleSearchIndex(String str, String label, Long time, Integer count) throws IOException, ParseException {
        BooleanQuery.Builder parse = getSearchParse(str, label, 0L);
        Directory dir = FSDirectory.open(Paths.get("E://luence/articleEntity"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(parse.build(), count);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<ArticleLucene> artList = new ArrayList<>();
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                ArticleLucene articleLucene = new ArticleLucene();
                int socID = scoreDoc.doc;
                Document doc = indexSearcher.doc(socID);
                articleLucene.setArticleId(Integer.parseInt(doc.get("id")));
                articleLucene.setTitle(doc.get("title"));
                articleLucene.setContent(getContent(doc.get("content"), 50));
                articleLucene.setPicturePath(doc.get("picturePath"));
                articleLucene.setReleaseTime(Long.parseLong(doc.get("releaseTime")));
                artList.add(articleLucene);
            }
        }
        indexReader.close();
        return artList;
    }

    /**
     * 文章内容展示到界面
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

    /**
     * 根据搜索条件创建Query搜索对象，默认根据标题，文章内容进行搜索
     * @param str
     * @param label
     * @param time
     * @return
     * @throws ParseException
     */
    private BooleanQuery.Builder getSearchParse(String str, String label, Long time) throws ParseException {
        Analyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("title", analyzer);
        Query parse1 = queryParser.parse("title:" + str);
        Query parse2 = queryParser.parse("content:" + str);
        Query parse3 = null;
        if (!label.equals("")) parse3 = queryParser.parse("label:" + label);
        Query parse4 = null;
        if (time != 0) parse4 = LongPoint.newRangeQuery("releaseTime", System.currentTimeMillis() - time, System.currentTimeMillis());
        BooleanQuery.Builder parse = new BooleanQuery.Builder();
        parse.add(parse1, BooleanClause.Occur.SHOULD);
        parse.add(parse2, BooleanClause.Occur.SHOULD);
        if (parse3 != null) parse.add(parse3, BooleanClause.Occur.SHOULD);
        if (parse4 != null) parse.add(parse4, BooleanClause.Occur.MUST);
        return parse;
    }
}
