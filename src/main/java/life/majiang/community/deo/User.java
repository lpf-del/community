package life.majiang.community.deo;

import lombok.Data;

@Data
public class User {
    private Long id;
    private Long accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
