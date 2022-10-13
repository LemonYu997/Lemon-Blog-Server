package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lemonyu997.top.lemonapi.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    //根据分类id查询对应的文章数
    Integer countArticleByCategoryId(Integer categoryId);
}
