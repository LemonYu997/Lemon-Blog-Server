package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//博客详情
@Data
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long articleCounts;      //文章总数
    private Long viewCounts;         //总阅读量
    private Long wordCounts;         //总字数
    private Long createDate;         //博客创建时间
}
