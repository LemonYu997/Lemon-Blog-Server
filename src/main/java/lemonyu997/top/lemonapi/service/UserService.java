package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.vo.UserVO;

public interface UserService {
    //根据账号和密码查询用户
    UserVO findUserByAP(String account, String password);

    //根据token获取用户
    UserVO findUserByToken(String token);
}
