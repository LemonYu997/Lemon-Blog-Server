package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lemonyu997.top.lemonapi.pojo.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    //根据文章id删除关联表中的行
    void deleteByArticleId(Long id);
}
