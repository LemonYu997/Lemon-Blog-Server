package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.pojo.Category;
import lemonyu997.top.lemonapi.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    //根据分类id获取分类
    CategoryVO findCategoryById(Integer categoryId);

    //获取所有分类，用于首页展示
    List<Category> findAllCategory();
}
