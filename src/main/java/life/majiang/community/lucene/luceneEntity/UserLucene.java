package life.majiang.community.lucene.luceneEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 用户搜索展示
 * @date 2022/1/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLucene {
    private Integer id;
    private String username;
    private String introduction;
    private Integer postCount;
    private String touXiangUrl;
}
