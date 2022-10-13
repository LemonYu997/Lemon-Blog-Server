package lemonyu997.top.lemonapi.vo;

import lombok.Data;

@Data
public class CommentVO {
    private Long id;                //Id
    private String avatar;          //评论者头像
    private String content;         //评论内容
    private String nickname;        //评论者昵称

    //将时间戳转为字符串
    private String createDate;        //评论时间
}
