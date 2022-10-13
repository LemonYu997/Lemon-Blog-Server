package lemonyu997.top.lemonapi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;                //Id
    private String avatar;          //评论者头像
    private String content;         //评论内容
    private Long createDate;        //评论时间
    private String email;           //评论者邮箱
    private String nickname;        //评论者昵称
    private Long articleId;         //评论的文章
    private Long parentCommentId;   //父评论的id
    private Integer level;          //评论级别（第几级评论）
}
