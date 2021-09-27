package life.majiang.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 文章的新类
 * 代替qusetion，是qusetion的扩展增加了许多新的内容
 * @author lpf
 * @description 文章
 * @date 2021/9/21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleEntity {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;               //发表文章的id，自增主键
    private String title;             //文章标题
    private String picturePath;       //图片路径，将第一张图片当作封面
    private String label;             //标签用#分割标签 建议三到四个
    private String classification;    //分类
    private String type;              //文章类型：原创，转载，翻译
    private String releaseForm;       //发布形式：公开，私密
    private String content;           //文章内容
    private Integer authorId;         //作者id
    private Integer x;                //0草稿箱， 1发布
}
