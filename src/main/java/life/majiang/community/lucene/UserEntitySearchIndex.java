package life.majiang.community.lucene;

import life.majiang.community.lucene.LuenceInterface.SearchIndexLuence;
import life.majiang.community.lucene.luceneEntity.UserLucene;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
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
 * @description 用户的索引查询
 * @date 2022/1/3
 */
@Component
public class UserEntitySearchIndex extends SearchIndexLuence {

    /**
     * 标准搜索：使用用户名搜索
     * 其他搜索：使用电话或者uuid搜索
     * 三种搜索方式只能使用一种
     * @param username
     * @param uuid
     * @param telephone
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<UserLucene> userSearchIndex(String username, String uuid, String telephone) throws IOException, ParseException {
        Analyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("username", analyzer);
        BooleanQuery.Builder parse = new BooleanQuery.Builder();
        if (!uuid.equals("")) {
            Query parse1 = queryParser.parse("uuid:" + uuid);
            parse.add(parse1, BooleanClause.Occur.MUST);
        }
        else if (!telephone.equals("")) {
            Query parse1 = queryParser.parse("telephone:" + telephone);
            parse.add(parse1, BooleanClause.Occur.MUST);
        }
        else {
            Query parse1 = queryParser.parse("username:" + username);
            parse.add(parse1, BooleanClause.Occur.SHOULD);
        }
        Directory dir = FSDirectory.open(Paths.get("E://luence/userEntity"));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(parse.build(), 20);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<UserLucene> userList = new ArrayList<>();
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                UserLucene userLucene = new UserLucene();
                int socID = scoreDoc.doc;
                Document doc = indexSearcher.doc(socID);
                if (doc == null) return userList;
                userLucene.setId(Integer.parseInt(doc.get("id")));
                userLucene.setIntroduction(doc.get("introduction"));
                userLucene.setPostCount(Integer.parseInt(doc.get("postCount")));
                userLucene.setUsername(doc.get("username"));
                userLucene.setTouXiangUrl(doc.get("touXiangUrl"));
                userList.add(userLucene);
            }
        }
        indexReader.close();
        return userList;
    }
}
