package life.majiang.community.deo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
    private Long id;
    private Long parentId;//父评论id
    private Long type;//父类类型（文章的评论，评论的评论）
    private Long commentator;//评论人id
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
}
