package life.majiang.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 文章的排行
 * @date 2021/9/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleRankingEntity {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;             //文章排行id
    private Integer authorId;       //作者id
    private Integer visits;         //访问量
    private Integer praiseQuantity; //点赞量
    private Integer comment;        //评论量
    private Long releaseTime;       //发布时间
    private Integer articleId;      //文章id
}
