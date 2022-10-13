package lemonyu997.top.lemonapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lemonyu997.top.lemonapi.mapper.UserMapper;
import lemonyu997.top.lemonapi.pojo.User;
import lemonyu997.top.lemonapi.service.UserService;
import lemonyu997.top.lemonapi.utils.JWTUtils;
import lemonyu997.top.lemonapi.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //根据用户名和密码查询用户
    @Override
    @Transactional  //登录方法使用事务
    public UserVO findUserByAP(String account, String password) {
        UserVO userVO = new UserVO();

        //查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account);
        queryWrapper.eq(User::getPassword, password);
        queryWrapper.last("limit 1");

        User user = userMapper.selectOne(queryWrapper);

        //如果成功查询到用户
        if (user != null) {
            BeanUtils.copyProperties(user, userVO);

            //登录成功后，更新上次的登录时间
            user.setLastLogin(System.currentTimeMillis());
            userMapper.updateById(user);
            return userVO;
        }

        //没有查询到用户，返回空
        return null;
    }

    //根据token获取用户
    @Override
    @Transactional  //登录方法使用事务
    public UserVO findUserByToken(String token) {
        //1、校验token
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }

        //2、解析token，从redis中获取用户json数据
        //前端会把token放在浏览器的localStorage中，发送登录请求时携带
        //后端将根据redis中的token查询对应的用户信息，如果过期了，就要重新登录
        String userJson =  redisTemplate.opsForValue().get("TOKEN_" + token) + "";
        if (StringUtils.isBlank(userJson)) {
            return  null;
        }

        //将json数据转为对象
        UserVO userVO = JSON.parseObject(userJson, UserVO.class);

        //成功解析
        return userVO;
    }




}
