package lemonyu997.top.lemonapi.thread;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lemonyu997.top.lemonapi.mapper.ArticleMapper;
import lemonyu997.top.lemonapi.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    //更新文章阅读量
    //此操作在线程池中执行，不会影响原本的主线程
    @Async("taskExecutor")  //从线程池中获取线程
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        //获取阅读量
        int viewCounts = article.getViewCounts();
        //重新new一个对象，减少要更新的字段
        Article articleUpdate = new Article();
        //阅读数+1
        articleUpdate.setViewCounts(viewCounts + 1);
        //封装更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //设置一个 在多线程环境下的线程安全
        updateWrapper.eq(Article::getViewCounts, viewCounts);
        //update article set view_count=100 where view_count = 99 and id = #{id}

        //执行更新操作
        articleMapper.update(articleUpdate, updateWrapper);
    }

    //增加文章评论数
    //此操作在线程池中执行，不会影响原本的主线程
    @Async("taskExecutor")  //从线程池中获取线程
    public void updateArticleCommentCount(ArticleMapper articleMapper, Article article) {
        //重新new一个对象，减少要更新的字段
        Article articleUpdate = new Article();
        //评论数+1
        articleUpdate.setCommentCounts(article.getCommentCounts() + 1);
        //封装更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //设置一个 在多线程环境下的线程安全
        updateWrapper.eq(Article::getCommentCounts, article.getCommentCounts());
        //update article set comment_counts=100 where comment_counts = 99 and id = #{id}

        //执行更新操作
        articleMapper.update(articleUpdate, updateWrapper);
    }

    //减少文章评论数
    //此操作在线程池中执行，不会影响原本的主线程
    @Async("taskExecutor")  //从线程池中获取线程
    public void updateArticleCommentCountForDelete(ArticleMapper articleMapper, Article article) {
        //重新new一个对象，减少要更新的字段
        Article articleUpdate = new Article();
        //评论数-1
        articleUpdate.setCommentCounts(article.getCommentCounts() - 1);
        //封装更新条件
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //设置一个 在多线程环境下的线程安全
        updateWrapper.eq(Article::getCommentCounts, article.getCommentCounts());
        //update article set comment_counts=100 where comment_counts = 99 and id = #{id}

        //执行更新操作
        articleMapper.update(articleUpdate, updateWrapper);
    }
}
