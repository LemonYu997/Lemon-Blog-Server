package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lemonyu997.top.lemonapi.pojo.ArticleBody;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleBodyMapper extends BaseMapper<ArticleBody> {
    //根据文章id查询正文内容
    String findContentByArticleId(Long articleId);
}
