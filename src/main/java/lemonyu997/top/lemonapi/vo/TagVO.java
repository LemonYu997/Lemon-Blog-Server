package lemonyu997.top.lemonapi.vo;

import lombok.Data;

@Data
public class TagVO {
    private Long id;                //Id
    private String tagName;         //标签名
    private Integer articleCounts;  //文章数量
}
