package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lemonyu997.top.lemonapi.pojo.Article;
import lemonyu997.top.lemonapi.vo.ArchiveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    //总字数求和
    Long getAllWordCounts();

    //分页查询，根据相关条件
    IPage<Article> listArticle(Page<Article> page, Integer categoryId, Long tagId, String year, String month);

    //获取首页展示的文章归档信息
    List<ArchiveVO> getArchiveInfoList();

    Integer countArticleByTime(@Param("year") Integer year, @Param("month") Integer month);

    //总浏览量求和，实际没用到
//    Long getAllViewCounts();
}
