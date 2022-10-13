package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.pojo.Comment;
import lemonyu997.top.lemonapi.vo.CommentVO;
import lemonyu997.top.lemonapi.vo.admin.CommentAdminVO;

import java.util.List;

public interface CommentService {
    //根据评论id查询评论
    Comment findCommentById(Long id);

    //根据文章id查询评论
    List<CommentVO> findCommentListByArticleId(Long articleId);

    //获得所有评论
    List<CommentAdminVO> findAllCommentList();

    //发表评论
    void addComment(Comment comment);

    //根据id删除评论
    void deleteCommentById(Long id);
}
