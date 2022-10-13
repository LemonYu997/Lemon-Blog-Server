package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;                    //id
    private String firstpicture;        //头图链接
    private String title;               //文章标题
    private String summary;             //摘要
    private Long bodyId;                //文章内容id
    private Integer authorId;           //作者id
    private Integer categoryId;         //分类id
    private Integer commentCounts;      //评论数量
    private Integer viewCounts;         //阅读量
    private Integer wordCounts;         //字数
    private Integer weight;             //置顶
    private Long createDate;            //创建时间
}
