package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.anno.LogAnnotation;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.ArticleService;
import lemonyu997.top.lemonapi.vo.ArchiveVO;
import lemonyu997.top.lemonapi.vo.ArticleVO;
import lemonyu997.top.lemonapi.vo.admin.ArticleAdminVO;
import lemonyu997.top.lemonapi.vo.params.ArticleParams;
import lemonyu997.top.lemonapi.vo.params.PageParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //根据文章id查询文章详情
    @GetMapping("/view/{id}")
    public Result getArticleDetailById(@PathVariable("id") Long articleId) {
        ArticleVO articleVO = articleService.findArticleDetailById(articleId);

        return Result.success(articleVO);
    }

    //分页查询，可根据分类、标签、时间进行查询
    //因为要传递分页参数，所以要用post
    @PostMapping("/list")
    public Result getArticleList(@RequestBody PageParams pageParams) {
        List<ArticleVO> articleVOList = articleService.listArticle(pageParams);

        return Result.success(articleVOList);
    }

    //首页显示的最新文章列表，默认取前8条
    @GetMapping("/new")
    public Result getNewArticleList() {
        int limit = 8;
        List<ArticleVO> articleVOList = articleService.newArticle(limit);
        return Result.success(articleVOList);
    }

    //首页显示的文章归档预览（只需要年月和文章数）
    @GetMapping("/allArchiveInfo")
    public Result getArchiveInfoList() {
        List<ArchiveVO> archiveVOList = articleService.getArchiveInfoList();

        return Result.success(archiveVOList);
    }

    //根据文章发布的年月获得文章归档信息
    @GetMapping("/archiveInfo/{year}/{month}")
    public Result getArchiveInfoByTime(@PathVariable("year") String year, @PathVariable("month") String month) {
        ArchiveVO archiveVO = articleService.findArchiveInfoByTime(year, month);
        return Result.success(archiveVO);
    }

    //TODO 缓存相关，将文章放入缓存，有更新的时候通知更新缓存，之后再实现

    //发布文章
    @PostMapping("/publish")
    @RequireLogin(true)
    @LogAnnotation(module = "文章", operation = "发布")
    public Result publishArticle(@RequestBody ArticleParams articleParams) {
//        articleService.addArticle(articleVO);

        //后端校验
        if (StringUtils.isBlank(articleParams.getTitle())
                || StringUtils.isBlank(articleParams.getFirstpicture())
                || StringUtils.isBlank(articleParams.getSummary())
                || StringUtils.isBlank(articleParams.getContent())) {
            return Result.fail(ErrorCode.PUBLISH_FAIL.getCode(), ErrorCode.PUBLISH_FAIL.getMsg());
        }

        //添加成功后返回一个文章id
        Map<String, String> articleIdMap = articleService.addArticle(articleParams);

        //返回成功的id，方便进行页面跳转
        return Result.success(articleIdMap);
    }

    //获取所有文章，用于后台展示
    @GetMapping("/admin/all")
    @RequireLogin(true)
    public Result getAllArticleForAdmin() {
        List<ArticleAdminVO> articleAdminVOList = articleService.findAllArticleForAdmin();

        return Result.success(articleAdminVOList);
    }

    //根据id更新文章
    @PutMapping("/update")
    @RequireLogin(true)
    @LogAnnotation(module = "文章", operation = "更新")
    public Result updateArticleById(@RequestBody ArticleParams articleParams) {
        //后端校验
        if (StringUtils.isBlank(articleParams.getTitle())
                || StringUtils.isBlank(articleParams.getFirstpicture())
                || StringUtils.isBlank(articleParams.getSummary())
                || StringUtils.isBlank(articleParams.getContent())) {
            return Result.fail(ErrorCode.PUBLISH_FAIL.getCode(), ErrorCode.PUBLISH_FAIL.getMsg());
        }

//        System.out.println(articleParams);

        //更新文章，返回文章id
        Map<String, String> articleIdMap = articleService.updateArticle(articleParams);
        return Result.success(articleIdMap);
    }

    //根据id删除文章
    @DeleteMapping("/delete/{id}")
    @RequireLogin(true)
    @LogAnnotation(module = "文章", operation = "删除")
    public Result deleteArticleById(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);

        return Result.success(null);
    }

    //TODO 实现浏览量增加
}
