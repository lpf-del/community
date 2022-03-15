package life.majiang.community.controller;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.lucene.ArticleEntityCreateIndex;
import life.majiang.community.lucene.CommentEntityCreateIndex;
import life.majiang.community.lucene.UserEntityCreateIndex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lpf
 * @description 索引的添加控制层
 * @date 2022/1/4
 */
@Controller
@RequestMapping("/luence")
public class CreateLuceneController {

    @Resource
    private ArticleEntityCreateIndex articleEntityCreateIndex;

    @Resource
    private UserEntityCreateIndex userEntityCreateIndex;

    @Resource
    private CommentEntityCreateIndex commentEntityCreateIndex;

    /**
     * 创建所有对象的所有索引，包括用户表、评论表、文章表。
     * @throws IOException
     */
    @RequestMapping("/createIndex")
    public void createIndex() throws IOException {
        createArticleIndex();
        createCommentIndex();
        createUserIndex();
    }

    /**
     * 创建用户表的所有索引
     * @throws IOException
     */
    @RequestMapping("/createUserIndex")
    public void createUserIndex() throws IOException {
        userEntityCreateIndex.createIndexAll();
    }

    /**
     * 创建文章表的所有索引
     * @throws IOException
     */
    @RequestMapping("/createArticleIndex")
    public void createArticleIndex() throws IOException {
        articleEntityCreateIndex.createIndexAll();
    }

    /**
     * 创建评论表的所有所索引
     * @throws IOException
     */
    @RequestMapping("/createCommentIndex")
    public void createCommentIndex() throws IOException {
        commentEntityCreateIndex.createIndexAll();
    }
}
