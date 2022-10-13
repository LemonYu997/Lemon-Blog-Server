package lemonyu997.top.lemonapi.handler;

import lemonyu997.top.lemonapi.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//统计首页访问次数
@Component
public class RedisUrlCountInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    //在访问首页的时候，Redis计数+1
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据IP统计，相同IP一天内访问算一次，使用redis的set存储IP，因为可以判断是否包含元素
        String requestIP = IpUtils.getIpAddr(request);
        //看redis中是否存在该IP
        Boolean flag = redisTemplate.opsForSet().isMember("requestIP", requestIP);
        //如果不存在
        if (!flag) {
            //将IP存到redis中
            redisTemplate.opsForSet().add("requestIP", requestIP);
            //用redis计数，+1
            redisTemplate.boundValueOps("indexCount").increment(1);
        }

        return true;
    }

    //每天重置一次集合
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void setRedisSet() {
        redisTemplate.delete("requestIP");
    }
}
