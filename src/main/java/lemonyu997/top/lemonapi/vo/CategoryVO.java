package lemonyu997.top.lemonapi.vo;

import lombok.Data;

@Data
public class CategoryVO {
    private Integer id;             //id
    private String categoryName;    //分类名称

    //该分类对应的文章数量
    private Integer articleCounts;  //文章数量
}
