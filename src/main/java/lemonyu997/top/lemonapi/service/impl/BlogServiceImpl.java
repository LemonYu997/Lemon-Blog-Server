package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lemonyu997.top.lemonapi.mapper.ArticleMapper;
import lemonyu997.top.lemonapi.mapper.BlogMapper;
import lemonyu997.top.lemonapi.pojo.Blog;
import lemonyu997.top.lemonapi.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

//    //更新网站访问量
//    public void addViewCount() {
//        Blog blog = blogMapper.selectById(1);
//        blog.setViewCounts(blog.getViewCounts() + 1);
//        blogMapper.updateById(blog);
//    }

    public Blog getBlogDetail() {
        updateBlog();
        //返回博客基础信息，就一个博客
        //Id为1，直接返回即可
        Blog blog = blogMapper.selectById(1);

        return blog;
    }

    //每小时更新一次Blog数据
    @Scheduled(cron = "0 0 */1 * * ?")
    //不需要开启事务，这个小时没有更新成功那就下个小时再更新，不影响，顶多丢失一些访问量
    //TODO 可以设置一个消息通知，如果新发布文章了，就主动调用该方法更新一下
    public void updateBlog() {
        Blog blog = blogMapper.selectById(1);

        //将下边的数据存入缓存，定时更新，不需要每次刷新页面都获取
        //获得当前总字数，用于更新
        Long allWordCounts = articleMapper.getAllWordCounts();
        //获得当前总文章数，用于更新
        Long allArticleCounts = articleMapper.selectCount(new QueryWrapper<>());

        //总阅读量：先从redis中获取这段时间存储的数量
        Object indexCount = redisTemplate.opsForValue().get("indexCount");

        //如果redis中有缓存
        if (indexCount != null) {
            //将redis中存储的数据转为Long类型
            Long newView = Long.valueOf(String.valueOf(indexCount));
            //清空redis中存储的数据量，重新计算
            redisTemplate.delete("indexCount");
            redisTemplate.opsForValue().set("indexCount", "0", 1, TimeUnit.DAYS);
            blog.setViewCounts(blog.getViewCounts() + newView);
        } else {
            redisTemplate.opsForValue().set("indexCount", "0", 1, TimeUnit.DAYS);
        }

        //更新总字数、文章总数、总浏览量
        blog.setWordCounts(allWordCounts);
        blog.setArticleCounts(allArticleCounts);
        blogMapper.updateById(blog);
    }

//    @Scheduled(cron = "0 0 0 */1 * ?")
    public void test1() {
        //测试主动调用定时任务
        System.out.println("这是一个主动开启的定时任务");
    }

//    @Scheduled(cron = "*/5 * * * * ?")
    public void test2() {
        //测试被动调用定时任务
        System.out.println("这是一个被动开启的定时任务");
    }
}
