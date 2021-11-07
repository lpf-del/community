package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 评论和评论人绑定
 * @date 2021/10/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentAndUser {
    private Integer commentId;          //文章id
    private CommentEntity commentEntity;//评论
    private UserEntity userEntity;      //评论人
}
