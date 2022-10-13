package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.anno.LogAnnotation;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.pojo.Recommend;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.RecommendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    //根据id获得推荐详情
    @GetMapping("/{id}")
    public Result getRecommendById(@PathVariable("id") Integer id) {
        Recommend recommend = recommendService.findRecommendById(id);

        return Result.success(recommend);
    }

    //获得所有推荐
    @GetMapping("/all")
    public Result getRecommendList() {
        List<Recommend> recommendList = recommendService.findRecommendList();

        return Result.success(recommendList);
    }

    //添加推荐
    @PostMapping("/add")
    @RequireLogin(true)
    @LogAnnotation(module = "推荐", operation = "添加")
    public Result addRecommend(@RequestBody Recommend recommend) {
        //进行校验
        if (StringUtils.isBlank(recommend.getNickname())
                || StringUtils.isBlank(recommend.getContent())
                || StringUtils.isBlank(recommend.getPicture())
                || StringUtils.isBlank(recommend.getSrc())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        } else {
            //执行添加操作
            recommendService.addRecommend(recommend);

            return Result.success(null);
        }
    }

    //更新推荐
    @PutMapping("/update")
    @RequireLogin(true)
    @LogAnnotation(module = "推荐", operation = "更新")
    public Result updateRecommend(@RequestBody Recommend recommend) {
        //进行校验
        if (StringUtils.isBlank(recommend.getNickname())
                || StringUtils.isBlank(recommend.getContent())
                || StringUtils.isBlank(recommend.getPicture())
                || StringUtils.isBlank(recommend.getSrc())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        } else {
            //执行更新
            recommendService.updateRecommend(recommend);

            return Result.success(null);
        }
    }

    //根据id删除推荐
    @DeleteMapping("/delete/{id}")
    @RequireLogin(true)
    @LogAnnotation(module = "推荐", operation = "删除")
    public Result deleteRecommendById(@PathVariable("id") Integer id) {
        recommendService.deleteRecommendById(id);

        return Result.success(null);
    }

}
