package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//文章内容和文章关联表
@Data
public class ArticleBody {
    @TableId(type = IdType.AUTO)
    private Long id;                //Id
    private String content;         //文本内容
    private String contentHtml;     //Html形式的文本内容
    private Long articleId;         //对应的文章id
}
