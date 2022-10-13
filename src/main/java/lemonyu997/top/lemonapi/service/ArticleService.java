package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.vo.ArchiveVO;
import lemonyu997.top.lemonapi.vo.ArticleVO;
import lemonyu997.top.lemonapi.vo.admin.ArticleAdminVO;
import lemonyu997.top.lemonapi.vo.params.ArticleParams;
import lemonyu997.top.lemonapi.vo.params.PageParams;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    //根据id返回文章对象，包含标签和分类信息
    ArticleVO findArticleDetailById(Long id);

    //返回分页查询结果
    List<ArticleVO> listArticle(PageParams pageParams);

    //最新文章
    List<ArticleVO> newArticle(int limit);

    //获得首页显示的文章归档信息
    List<ArchiveVO> getArchiveInfoList();

    //根据发布时间查找对应的归档信息
    ArchiveVO findArchiveInfoByTime(String year, String month);

    //添加文章操作，返回文章id
    Map<String, String> addArticle(ArticleParams articleParams);

    //根据id更新文章操作，返回文章id
    Map<String, String> updateArticle(ArticleParams articleParams);

    //获取所有文章，用于后台展示
    List<ArticleAdminVO> findAllArticleForAdmin();

    //根据id删除文章
    void deleteArticleById(Long id);
}
