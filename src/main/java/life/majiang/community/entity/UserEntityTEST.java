package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Date;
import java.io.Serializable;

/**
 * @author lpf
 * @description redis实体类
 * @date 2021/9/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntityTEST implements Serializable {
    private Long id;
    private String guid;
    private String name;
    private String age;
    private Date createTime;
}
