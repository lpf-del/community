package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 评论
 * @date 2021/9/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentEntity {
    private Integer commentId;        //评论id
    private Integer commentator;      //评论人
    private Integer reviewedByMan;    //被评论的
    private boolean commentType;   //评论类型，true评论评论，false评论文章
    private String commentContent; //评论内容
    private String commentPicture; //评论图片，最多一张
}
