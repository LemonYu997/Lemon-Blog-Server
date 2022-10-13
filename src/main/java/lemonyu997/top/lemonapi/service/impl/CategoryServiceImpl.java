package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lemonyu997.top.lemonapi.mapper.CategoryMapper;
import lemonyu997.top.lemonapi.pojo.Category;
import lemonyu997.top.lemonapi.service.CategoryService;
import lemonyu997.top.lemonapi.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //根据分类id得到分类
    @Override
    public CategoryVO findCategoryById(Integer categoryId) {
        CategoryVO categoryVO = new CategoryVO();

        Category category = categoryMapper.selectById(categoryId);
        BeanUtils.copyProperties(category, categoryVO);

        //查询该分类包含的文章数量
        Integer count = categoryMapper.countArticleByCategoryId(categoryId);
        categoryVO.setArticleCounts(count);

        return categoryVO;
    }

    //获取所有分类
    @Override
    public List<Category> findAllCategory() {
        List<Category> categoryList = categoryMapper.selectList(new QueryWrapper<>());

        return categoryList;
    }
}
