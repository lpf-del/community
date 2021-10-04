package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 作者和文章的包装类
 * @date 2021/10/4
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleAndUserAndRang {
    private UserEntity userEntity;
    private ArticleEntity articleEntity;
    private ArticleRankingEntity articleRankingEntity;
}
