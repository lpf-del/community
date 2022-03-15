package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户评论通知
 * @date 2022/3/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCommentNotice {
    private String headPicture;    //头像
    private Long commentTime;      //评论发布时间
    private String articleTitle;   //文章标题 ，评论文章时使用
    private String commentTwo;     //评论的评论内容
    private String commnetorName;  //评论人的名称
    private Integer commentorId;   //评论人id
    private Integer articleId;     //文章id
    private String commentContent; //评论内容
}
