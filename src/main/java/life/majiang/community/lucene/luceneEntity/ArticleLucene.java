package life.majiang.community.lucene.luceneEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 文章搜索的展示所需要的属性
 * @date 2022/1/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleLucene {
    private Integer articleId;  //文章的id
    private String title;       //文章的标题
    private String picturePath; //文章的图片路径，用做封面
    private String content;     //文章的内容，只展示前50字并且去掉特殊字符
    private Long releaseTime;   //文章发布时间
}
