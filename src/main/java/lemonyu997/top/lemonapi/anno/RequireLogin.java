package lemonyu997.top.lemonapi.anno;

import java.lang.annotation.*;

//自定义登录拦截注解，给需要登录的Handler方法添加此注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) //运行时有效
@Documented
public @interface RequireLogin {
    //是否需要登录，默认不需要登录
    boolean value() default false;
}
