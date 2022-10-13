package lemonyu997.top.lemonapi.vo;

import lombok.Data;

@Data
public class UserVO {
    //返回给前端的字段，敏感信息不返回
    private Integer id;         //Id
    private String account;     //账户，登录用的
    private String avatar;      //头像
    private String nickname;    //昵称，显示在前端页面用
}
