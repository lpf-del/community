package life.majiang.community.lucene.luceneEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 新的评论展示对象
 * @date 2022/1/27
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPlusEntity {
    private Integer commentId;    //评论id
    private Integer commentator;  //评论人id
    private Integer articleId;    //评论的文章id
    private String commentContent;//评论的内容
    private Long commentTime;     //评论时间
    private String username;      //评论人名称
    private String articleTitle;  //文章标签
    private Integer commentType;  //评论类型
}
