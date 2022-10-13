package lemonyu997.top.lemonapi.vo.params;

import lombok.Data;


@Data
public class LoginParams {
    //TODO 登陆参数相关设置，目前只开放给自己使用，只需要账号密码，开放用户注册的时候要加验证码，防止无限请求API
    private String account;
    private String password;
}
