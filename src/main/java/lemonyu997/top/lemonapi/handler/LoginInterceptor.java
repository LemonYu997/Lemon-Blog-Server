package lemonyu997.top.lemonapi.handler;

import com.alibaba.fastjson.JSON;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.pojo.User;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.UserService;
import lemonyu997.top.lemonapi.utils.UserThreadLocal;
import lemonyu997.top.lemonapi.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//登录拦截器，对标注@RequireLogin注解的控制器方法进行拦截
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、判断请求的接口路径是否为 HandlerMethod (Controller方法)
        if (!(handler instanceof HandlerMethod)) {
            //handler可能是资源请求RequestResourceHandler，访问静态资源，放行
            return true;
        }

        //2、对加了RequireLogin注解的请求才执行登录拦截
        //得到现在的方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //获取现在方法的RequireLogin注解
        RequireLogin requireLogin = method.getAnnotation(RequireLogin.class);

        //如果没有加该注解，直接放行
        if (requireLogin == null) {
            return true;
        }

        //3、如果需要进行登录，就执行拦截操作
        if (requireLogin.value()) {
            //获取请求头中的token
            String token = request.getHeader("Authorization");

            //记录登录日志
            log.info("=================request start===========================");
            String requestURI = request.getRequestURI();
            log.info("request uri:{}", requestURI);
            log.info("request method:{}", request.getMethod());
            log.info("token:{}", token);
            log.info("=================request end===========================");

            //如果token为空，表示未登录，返回错误提示
            if (StringUtils.isBlank(token)) {
                Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
                //在响应体中返回错误信息
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }

            //4、如果token不为空，进行登录验证
            UserVO userVO = userService.findUserByToken(token);
            //如果没有查询到对应的用户，返回未登录错误提示
            if (userVO == null) {
                Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
                //在响应体中返回错误信息
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }

            //5、认证成功，在UserThreadLocal存放user信息，方便其他Controller方法进行调用
            User user = new User();
            BeanUtils.copyProperties(userVO, user);
            UserThreadLocal.put(user);

            //放行
            return true;
        }

        //其他情况放行
        return true;
    }

    //释放资源，在视图渲染完之后调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除ThreadLocal中用完的信息，会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
