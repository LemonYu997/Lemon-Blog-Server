package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.pojo.Category;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.CategoryService;
import lemonyu997.top.lemonapi.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //根据分类id获取分类信息，包含其文章数
    @GetMapping("/{id}")
    public Result getCategoryById(@PathVariable("id") Integer categoryId) {
        CategoryVO categoryVO = categoryService.findCategoryById(categoryId);

        return Result.success(categoryVO);
    }

    //获取所有分类，用于首页展示
    @GetMapping("/all")
    public Result getAllCategory() {
        List<Category> categoryList = categoryService.findAllCategory();

        return Result.success(categoryList);
    }

    //根据分类id获取文章总数


    //TODO 添加、更新、删除接口暂不实现，直接写死
    //添加分类
    //更新分类     主要是级联操作
    //删除分类     主要是级联操作  默认无分类？再想想
}
