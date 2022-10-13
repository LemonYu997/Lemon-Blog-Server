package lemonyu997.top.lemonapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling   //开启定时任务
@EnableCaching      //开启缓存
@EnableTransactionManagement    //开启事务支持
@SpringBootApplication
public class LemonApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LemonApiApplication.class, args);
    }

}
