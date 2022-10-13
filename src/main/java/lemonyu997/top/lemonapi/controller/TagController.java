package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.anno.LogAnnotation;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.pojo.Tag;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.TagService;
import lemonyu997.top.lemonapi.vo.TagVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    //根据标签id获取标签
    @GetMapping("/{id}")
    public Result getTagById(@PathVariable("id") Long tagId) {
        TagVO tagVO = tagService.findTagById(tagId);

        return Result.success(tagVO);
    }

    //获取全部标签
    @GetMapping("/all")
    public Result getAllTag() {
        List<Tag> tagList = tagService.findAllTag();
        return Result.success(tagList);
    }

    //获取全部标签，用于后台展示，需要显示文章数
    @GetMapping("/admin/all")
    public Result getAllTagForAdmin() {
        List<TagVO> tagVOList = tagService.findAllTagForAdmin();

        return Result.success(tagVOList);
    }

    //添加标签
    @PostMapping("/add")
    @RequireLogin(true)
    @LogAnnotation(module = "标签", operation = "添加")
    public Result addTag(@RequestBody Tag tag) {
        //校验
        if (StringUtils.isBlank(tag.getTagName())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        tagService.addTag(tag);

        return Result.success(null);
    }

    //更新标签
    @PutMapping("/update")
    @RequireLogin(true)
    @LogAnnotation(module = "标签", operation = "更新")
    public Result updateTag(@RequestBody Tag tag) {
        //主要就是更新标签名，先做校验
        if (StringUtils.isBlank(tag.getTagName())) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        tagService.updateTag(tag);

        return Result.success(null);
    }

    //根据id删除标签
    @DeleteMapping("/delete/{id}")
    @RequireLogin(true)
    @LogAnnotation(module = "标签", operation = "删除")
    public Result deleteTagById(@PathVariable("id") Long id) {
        tagService.deleteById(id);

        return Result.success(null);
    }
}
