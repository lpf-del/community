package life.majiang.community.deo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long id;
    private Long notifier;
    private Long receiver;
    private Long type;
    private Long gmtCreate;
    private Boolean status;
    private String ification;
    private Long questionId;
}
