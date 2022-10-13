package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//用户表
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;         //Id
    private String account;     //账户，登录用的
    private String avatar;      //头像
    private Long createDate;    //创建时间
    private String email;       //邮箱
    private Long lastLogin;     //最后一次登录时间
    private String nickname;    //昵称，显示在前端页面用
    private String password;    //密码
    private String salt;        //加密盐
    private String status;      //状态
}
