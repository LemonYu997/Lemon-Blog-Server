package lemonyu997.top.lemonapi.controller;

import com.alibaba.fastjson.JSON;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.pojo.User;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.UserService;
import lemonyu997.top.lemonapi.utils.JWTUtils;
import lemonyu997.top.lemonapi.vo.UserVO;
import lemonyu997.top.lemonapi.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //用来存放token
    @Autowired
    private RedisTemplate redisTemplate;

    //加密盐，用来给密码加密
    private static final String SALT = "lemon!@#";

    //登录功能
    @PostMapping("/login")
    public Result login(@RequestBody LoginParams loginParams) {
        //1、检查参数是否合法
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

        //如果用户名和密码为空，返回错误
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        //2、对密码进行加密
        password = DigestUtils.md5Hex(password + SALT);

        //3、根据用户名和密码查询user表
        UserVO userVO = userService.findUserByAP(account, password);
        if (userVO == null) {
            //如果用户不存在，登录失败
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //4、如果用户存在，生成Token，返回给前端
        String token = JWTUtils.createToken(userVO.getId());
        //5、把token放入redis中，redis中设置token和user的映射关系，设置过期时间为1天
        //前端会把token放在浏览器的localStorage中，发送登录请求时携带
        //后端将根据redis中的token查询对应的用户信息，如果过期了，就要重新登录
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(userVO), 1, TimeUnit.DAYS);


        return Result.success(token);
    }

    //获取当前登录用户，从请求头中获取token
    @GetMapping("/currentUser")
    @RequireLogin(true)
    public Result currentUser(@RequestHeader("Authorization") String token) {
        UserVO userVO = userService.findUserByToken(token);
        if (userVO == null) {
            //查询失败
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        //查询成功，返回当前用户信息
        return Result.success(userVO);
    }


    //TODO 根据id查询
    //TODO 查询所有

    //TODO 虽然不用，但是先预留接口
    //TODO 添加用户
    //TODO 更新用户
    //TODO 删除用户    实际只改变数据库中状态，不实际删除，防止误操作
}
