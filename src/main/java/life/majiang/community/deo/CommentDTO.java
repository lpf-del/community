package life.majiang.community.deo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long parentId;
    private String content;
    private Long type;
}
