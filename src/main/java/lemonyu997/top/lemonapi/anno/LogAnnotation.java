package lemonyu997.top.lemonapi.anno;

import java.lang.annotation.*;

//AOP日志注解
//@Target表示可以放在方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    //声明要记录日志的模块名字
    String module() default "";
    //声明要执行的操作
    String operation() default "";
}
