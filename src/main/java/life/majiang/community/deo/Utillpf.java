package life.majiang.community.deo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Utillpf {
    private Long id;
    private String userName;
    private Boolean dianZan;
    private String pingLun;
    private Long wenZhangId;
}
