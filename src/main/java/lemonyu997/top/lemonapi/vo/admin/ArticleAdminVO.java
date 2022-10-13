package lemonyu997.top.lemonapi.vo.admin;

import lemonyu997.top.lemonapi.pojo.Category;
import lemonyu997.top.lemonapi.pojo.Tag;
import lombok.Data;

import java.util.List;

//用于后台展示列表信息
@Data
public class ArticleAdminVO {
    private Long id;
    private String title;               //文章标题
    private String firstpicture;        //头图链接
    private Integer wordCounts;         //字数
    private Integer viewCounts;         //阅读数
    private Integer commentCounts;      //评论数
    private String createDate;          //创建时间转为String类型，以yyyy-MM-dd HH:mm:ss格式呈现
    private List<Tag> tags;             //所属标签
    private Category category;          //所属分类
}
