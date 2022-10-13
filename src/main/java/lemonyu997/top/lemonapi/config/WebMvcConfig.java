package lemonyu997.top.lemonapi.config;

import lemonyu997.top.lemonapi.handler.LoginInterceptor;
import lemonyu997.top.lemonapi.handler.RedisUrlCountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //Filter和Interceptor功能几乎相同
    //Filter是Servlet定义的原生组件，脱离Spring也能使用
    //Interceptor是Spring定义的接口，可以使用Spring自动装配的功能
    @Autowired
    RedisUrlCountInterceptor redisUrlCountInterceptor;

    //登录拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加统计RedisUrl的拦截器
        registry.addInterceptor(redisUrlCountInterceptor)
                //只拦截获取博客详情的请求，减少拦截量
                .addPathPatterns("/blog");

        //注册登录拦截器，拦截所有请求，但实际被拦截的是加了@ReuqireLogin注解的请求映射处理器方法
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**");

    }

    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //端口不一致也算跨域，前后端分离项目，域名和端口可能不一致
        //设置跨域规则
        registry.addMapping("/**")
                .allowedOrigins("http://lemonyu997.top")
                .allowedOrigins("http://blog.lemonyu997.top");
    }
}
