package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lpf
 * @description 账号的信息
 * @date 2021/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInformation {
    private String qqTip;                //qq提示信息：修改qq，绑定qq
    private String weChatTip;            //微信提示信息：修改微信，绑定微信
    private String telephoneTip;         //电话提示信息：修改电话，绑定手机
    private String mailTip;              //邮箱提示信息：修改邮箱，绑定邮箱
    private Integer accountSafetyFactor; //安全系数
    private String accountSafes;         //如：为了更好的保障您账号的安全，请您继续完善： 绑定安全邮箱
    private String accountSafe;          //如：低风险
    private String passWordModification; //密码修改信息：修改密码
}
