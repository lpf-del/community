package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户的点赞日志
 * @date 2021/10/5
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPraiseQuantityLogEntity {
    private Integer upqlId;             //id
    private Integer authorId;           //被点赞的作者
    private Integer praiseQuantityType; //为1点赞文章，为0点赞评论
    private Integer praiseQuantityId;   //被点赞的id（文章id，或评论id）
    private Integer userId;             //点赞的用户
    private Long praiseQuantityTime;    //点赞时间
}
