package lemonyu997.top.lemonapi.vo.admin;

import lombok.Data;

//后台展示用的评论
@Data
public class CommentAdminVO {
    private Long id;                //Id
    private String avatar;          //评论者头像
    private String content;         //评论内容
    private String email;           //评论者邮箱
    private String nickname;        //评论者昵称
    private Long articleId;         //评论对应的文章id

    private String articleTitle;    //评论对应的文章标标题
    private String createDate;      //创建时间转为String类型，以yyyy-MM-dd HH:mm:ss格式呈现
}
