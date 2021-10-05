package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户评论的日志
 * @date 2021/10/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCommentLogEntity {
    private Integer uclId;      //id
    private Integer commentId;  //被评论的id（文章id，评论id）
    private Integer commentType;//评论的类型，为1代表文章，为0代表评论
    private Integer authorId;   //被评论的作者
    private Integer userId;     //评论的用户
    private Long commentTime;   //评论时间
}
