package life.majiang.community.lucene.luceneEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 评论展示对象
 * @date 2022/1/6
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentLucene {
    private Integer commentId;    //评论id
    private Integer commentator;  //评论人id
    private Integer articleId;    //评论的文章id
    private String commentContent;//评论的内容
    private Long commentTime;     //评论时间
}

