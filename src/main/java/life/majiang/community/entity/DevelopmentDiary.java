package life.majiang.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 开发日志
 * @date 2021/9/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DevelopmentDiary {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;  //开发日志id
    private String title;   //开发日志标题
    private String diary;   //日志内容
    private Long diaryTime; //日志时间
}
