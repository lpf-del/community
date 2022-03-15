package life.majiang.community.controller;

import life.majiang.community.lucene.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lpf
 * @description 删除所有索引的控制层
 * @date 2022/1/4
 */
@Controller
@RequestMapping("/lucene")
public class DeleteLuceneController {

    @Resource
    private ArticleEntityDeleteIndex articleEntityDeleteIndex;

    @Resource
    private UserEntiryDeleteIndex userEntiryDeleteIndex;

    @Resource
    private CommentEntityDeleteIndex commentEntityDeleteIndex;

    /**
     * 删除所有表的索引，包括用户表、文章表、评论表。
     * @throws IOException
     */
    @RequestMapping("/deleteIndex")
    public void deleteIndex() throws IOException {
        deleteArticleIndex();
        deleteCommentIndex();
        deleteUserIndex();
    }

    /**
     * 删除文章表的所有索引
     * @throws IOException
     */
    @RequestMapping("/deleteArticleIndex")
    public void deleteArticleIndex() throws IOException {
       articleEntityDeleteIndex.deleteIndexAll();
    }

    /**
     * 删除用户表的所有索引
     * @throws IOException
     */
    @RequestMapping("/deleteUserIndex")
    public void deleteUserIndex() throws IOException {
        userEntiryDeleteIndex.deleteIndexAll();
    }

    /**
     * 删除评论表的所有索引
     * @throws IOException
     */
    @RequestMapping("/deleteCommentIndex")
    public void deleteCommentIndex() throws IOException {
        commentEntityDeleteIndex.deleteIndexAll();
    }

    /**
     * 删除单个评论对象的索引
     * @param i
     * @throws IOException
     */
    @RequestMapping("/delCommentIndex")
    public void delCommentIndex(Integer i) throws IOException {
        commentEntityDeleteIndex.deleteIndex(i);
    }

    /**
     * 删除单个文章对象的索引
     * @param i
     * @throws IOException
     */
    @RequestMapping("/delArticleIndex")
    public void delArticleIndex(Integer i) throws IOException {
        articleEntityDeleteIndex.deleteIndex(i);
    }

    /**
     * 删除单个用户对象的索引
     * @param i
     * @throws IOException
     */
    @RequestMapping("/delUserIndex")
    public void delUserIndex(Integer i) throws IOException {
        userEntiryDeleteIndex.deleteIndex(i);
    }
}
