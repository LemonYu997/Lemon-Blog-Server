package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//文章和标签关联表
@Data
public class ArticleTag {
    @TableId(type = IdType.AUTO)
    private Long id;            //id
    private Long articleId;     //文章id
    private Long tagId;         //标签id
}
