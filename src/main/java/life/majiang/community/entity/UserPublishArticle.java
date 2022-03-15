package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户文章发布对象
 * @date 2022/3/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPublishArticle {
    private String headPicture;    //头像
    private String picture;        //文章图片
    private Long articleTime;      //文章发布时间
    private String articleTitle;   //文章标题
    private String authorName;     //作者名称
    private Integer authorId;      //作者id
    private Integer articleId;     //文章id
    private String articleContent;//文章内容
    private Integer praiseQuantity;//点赞量
    private Integer comment;       //评论量
}
