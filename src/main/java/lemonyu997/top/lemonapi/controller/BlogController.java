package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.pojo.Blog;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

//    //统计博客访问量
//    @GetMapping("/")
//    public Result addViewCount() {
//        blogService.addViewCount();
//
//        return Result.success(null);
//    }

    //返回博客详情
    @GetMapping("/blog")
    public Result getBlogDetail() {
        Blog blog = blogService.getBlogDetail();
        return Result.success(blog);
    }
}
