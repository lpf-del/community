package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentAndUser;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.mapper.CommentEntityMapper;
import life.majiang.community.service.CommentEntityService;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lpf
 * @description 新的评论类
 * @date 2021/9/26
 */
@Service
public class CommentEntityServiceImpl extends ServiceImpl<CommentEntityMapper, CommentEntity> implements CommentEntityService {

    @Resource
    private CommentEntityMapper commentEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private CookieService cookieService;


    @Override
    public List<CommentAndUser> getFiveComment(Integer articleId, Integer page) {
        List<CommentAndUser> list = getCommentByArticle(articleId);
        List<CommentAndUser> collect = list.stream()
                .filter(s -> s.getCommentEntity().getCommentType() == 1).collect(Collectors.toList());
        return getCommentByPage(collect, page);
    }

    /**
     * 获取指定页数的评论（只有评论文章的才会分页）
     * @param collect
     * @return
     */
    private List<CommentAndUser> getCommentByPage(List<CommentAndUser> collect, Integer page) {
        int size = collect.size();
        if (page * 5 > size) page = size / 5 + size % 5 == 0 ? 0 : 1;
        int left = (page - 1) * 5, right = (page * 5) > size ? size : (page * 5);
        List<CommentAndUser> list = collect.subList(left, right);
        return list;
    }

    /**
     * 获取该文章的所有评论
     * @param articleId
     * @return
     */
    private List<CommentAndUser> getCommentByArticle(Integer articleId) {
        Object o = redisUtil.get("a_i_c_" + articleId);
        if (o == null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("article_id", articleId);
            List<CommentAndUser> list = commentAndUser(commentEntityMapper.selectByMap(map));
            redisUtil.set("a_i_c_" + articleId, JSON.toJSONString(list));
            return list;
        }
        return (List<CommentAndUser>) JSON.parseObject(o.toString(), List.class);
    }

    @Override
    public List<CommentAndUser> getCommentAll(Integer articleId, Integer commentId) {
        List<CommentAndUser> commentByArticle = getCommentByArticle(articleId);
        List<CommentAndUser> collect = commentByArticle.stream().filter(s -> s.getCommentEntity()
                .getCommentType() == 0 && s.getCommentEntity().getCommentId().equals(commentId))
                .collect(Collectors.toList());
        return collect;
    }


    @Override
    public void addComment(Integer articleId, Integer commentId, String comment, HttpServletRequest request) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setArticleId(articleId);//文章id
        commentEntity.setCommentator(cookieService.getPersonInformation(request).getUserId());//评论人id
        commentEntity.setCommentContent(comment);//评论内容
        commentEntity.setCommentPicture("");//评论的图片
        commentEntity.setCommentTime(System.currentTimeMillis());//评论时间
        commentEntity.setCommentType(commentId==0?0:1);//评论种类
        commentEntity.setReviewedByMan(commentId==0?articleId:commentId);//被评论的id
        commentEntityMapper.insert(commentEntity);
    }

    /**
     * 评论和user（评论人绑定）：主要使用姓名，头像（后续加入），也可能使用地址
     * @param records
     * @return
     */
    private List<CommentAndUser> commentAndUser(List<CommentEntity> records) {
        List<CommentAndUser> list = new ArrayList<>();
        for (CommentEntity record : records) {
            CommentAndUser commentAndUser = new CommentAndUser(record,
                    userEntityService.getAuthor(record.getCommentator()));
            list.add(commentAndUser);
        }
        return list;
    }
}
