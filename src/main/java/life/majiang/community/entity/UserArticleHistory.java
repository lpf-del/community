package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 文章观看历史对象
 * @date 2022/3/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserArticleHistory {
    private String picture;    //主题图片
    private String title;      //标题
    private String authorName; //文章作者名字
    private Integer authorId;  //文章作者id
    private Integer articleId; //文章id
    private Long time;         //观看时间
}
