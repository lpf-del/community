package life.majiang.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lpf
 * @description user
 * 新的user类， 包括user所有信息
 * @date 2021/9/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity implements Serializable {
    private Integer userId;         //用户id自增
    private String userName;        //用户名
    private String passWord;        //密码（明文）
    private String mail;            //邮箱
    private String telephone;       //电话号码
    private Integer medal;          //勋章 int的包装类每一位是一个勋章
    private String qq;              //qq号码
    private String weChat;          //微信号码
    private String introduction;    //简介（不超过100字节）
    private String address;         //地址
    private Long registerTime;      //注册时间
    private Integer likeCount;      //点赞数
    private Integer postCount;      //发帖数量
    private Integer heatNumber;     //热度
    private String uuid;            //唯一标识
}
