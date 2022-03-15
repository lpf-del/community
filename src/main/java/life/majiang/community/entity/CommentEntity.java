package life.majiang.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;               //评论id
    private Integer commentator;      //评论人
    private Integer reviewedByMan;    //被评论的:当评论文章时为文章的id，当评论评论时是评论的id
    private Integer articleId;        //评论的文章
    private Integer commentType;      //评论类型，true评论评论，false评论文章
    private String commentContent;    //评论内容
    private String commentPicture;    //评论图片，最多一张
    private Integer likeCount;        //点赞数量
    private Long commentTime;         //评论时间
}
