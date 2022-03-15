package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户点赞通知
 * @date 2022/3/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserHeartNotice {
    private String headPicture;    //头像
    private Long HeartTime;        //点赞时间
    private String articleTitle;   //文章标题 ，点赞文章时使用
    private String heartName;      //点赞人的名称
    private Integer heartId;       //点赞人id
    private Integer articleId;     //文章id
    private String commentContent; //评论内容， 点赞评论时使用
}
