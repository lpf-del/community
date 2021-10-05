package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户对文章的访问日志
 * @date 2021/10/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserArticleVisitLogEntity {
    private Integer uavlId;   //id
    private Integer userId;   //访问用户的id
    private Integer articleId;//文章的id
    private Long visitTime;   //访问时间
}
