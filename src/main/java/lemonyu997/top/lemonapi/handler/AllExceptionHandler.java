package lemonyu997.top.lemonapi.handler;

import lemonyu997.top.lemonapi.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理
//对添加了@Controller注解方法进行拦截处理，用AOP实现
@ControllerAdvice
@Slf4j
public class AllExceptionHandler {
    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody   //返回Json数据
    public Result doException(Exception e) {
        //记录异常日志
        log.info("**********************Exception Start**********************");
        log.error("发生异常：" + e.getMessage());
        log.error("原因是：");
        e.printStackTrace();
        log.info("**********************Exception end**********************");

        return Result.fail(-999, "系统异常");
    }
}
