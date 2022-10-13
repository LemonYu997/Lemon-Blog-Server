package lemonyu997.top.lemonapi.aop;

import com.alibaba.fastjson.JSON;
import lemonyu997.top.lemonapi.anno.LogAnnotation;
import lemonyu997.top.lemonapi.utils.HttpContextUtils;
import lemonyu997.top.lemonapi.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

//用aop思想实现记录日志的功能
@Component
@Aspect
@Slf4j
public class LogAspect {

    //定义切点
    @Pointcut("@annotation(lemonyu997.top.lemonapi.anno.LogAnnotation)")
    public void pt() { }

    //环绕通知
    @Around("pt()")
    public Object myLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //记录执行时间
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(joinPoint, time);
        //原样返回
        return result;
    }

    //记录日志的方法
    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        //通过反射得到方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //得到注解
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        //开始记录
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module());
        log.info("operation:{}",logAnnotation.operation());

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        //获取请求地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));

        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");
    }
}
