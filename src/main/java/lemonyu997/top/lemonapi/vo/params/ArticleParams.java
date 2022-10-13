package lemonyu997.top.lemonapi.vo.params;

import lombok.Data;

import java.util.List;

//发布文章时接收的数据
@Data
public class ArticleParams {
    private Long id;
    private String firstpicture;        //头图链接
    private String title;               //文章标题
    private String summary;             //摘要
    private Integer wordCounts;         //字数
    //TODO 置顶功能暂未实现，先预留参数
    private Integer weight;             //置顶
    private String content;             //文章内容
    private Integer categoryId;         //分类Id
    private List<String> tagsName;      //标签名
}
