package lemonyu997.top.lemonapi.vo;

import lemonyu997.top.lemonapi.pojo.Category;
import lemonyu997.top.lemonapi.pojo.Tag;
import lombok.Data;

import java.util.List;

//实际和页面交互的数据
@Data
public class ArticleVO {
    private Long id;
    private String firstpicture;        //头图链接
    private String title;               //文章标题
    private String summary;             //摘要
    private Integer commentCounts;      //评论数量
    private Integer viewCounts;         //阅读量
    private Integer wordCounts;         //字数
    private Integer weight;             //置顶
    private Integer authorId;           //作者id，方便后期检索到作者进行修改
    private String createDate;          //创建时间转为String类型，以yyyy-MM-dd HH:mm:ss格式呈现

    //一些关联数据
    private String author;              //作者昵称
    private String content;             //文章内容
    private List<Tag> tags;             //所属标签
    private Category category;          //所属分类
}
