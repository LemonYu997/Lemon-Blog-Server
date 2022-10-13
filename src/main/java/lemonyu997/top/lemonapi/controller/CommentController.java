package lemonyu997.top.lemonapi.controller;

import lemonyu997.top.lemonapi.anno.LogAnnotation;
import lemonyu997.top.lemonapi.anno.RequireLogin;
import lemonyu997.top.lemonapi.pojo.Comment;
import lemonyu997.top.lemonapi.result.ErrorCode;
import lemonyu997.top.lemonapi.result.Result;
import lemonyu997.top.lemonapi.service.CommentService;
import lemonyu997.top.lemonapi.vo.CommentVO;
import lemonyu997.top.lemonapi.vo.admin.CommentAdminVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //根据评论id查询评论
    @GetMapping("/{id}")
    public Result getCommentById(@PathVariable("id") Long id) {
        Comment comment = commentService.findCommentById(id);

        return Result.success(comment);
    }

    //根据文章id查询所有评论，展示在页面上
    @GetMapping("/article/{id}")
    public Result getCommentListByArticleId(@PathVariable("id") Long articleId) {
        List<CommentVO> commentVOList =  commentService.findCommentListByArticleId(articleId);
        return Result.success(commentVOList);
    }

    //查询所有评论，用在后台展示
    @GetMapping("/all")
    public Result getAllCommentList() {
        List<CommentAdminVO> commentAdminVOList = commentService.findAllCommentList();

        return Result.success(commentAdminVOList);
    }

    //发布评论
    @PostMapping("/publish")
    @LogAnnotation(module = "评论", operation = "发布")
    public Result addComment(@RequestBody Comment comment) {

        //先进行验证，必须有昵称、邮箱
        if (StringUtils.isBlank(comment.getNickname()) || StringUtils.isBlank(comment.getEmail()) ) {
            return Result.fail(ErrorCode.COMMENT_EMAIL_OR_NAME_NOT_EXIST.getCode(), ErrorCode.COMMENT_EMAIL_OR_NAME_NOT_EXIST.getMsg());
        }

        //评论内容不能为空
        if (StringUtils.isBlank(comment.getContent())) {
            return Result.fail(ErrorCode.COMMENT_CONTENT_NOT_EXIST.getCode(), ErrorCode.COMMENT_CONTENT_NOT_EXIST.getMsg());
        }

        //邮箱正则校验
        String EmailReg = "[a-zA-Z0-9]{3,20}@([a-zA-Z0-9]{2,10}|[a-zA-Z0-9]{2,10}[.][a-zA-Z0-9]{2,10})[.](com|cn|net)";
        if (!comment.getEmail().matches(EmailReg)) {
            return Result.fail(ErrorCode.EMAIL_ERROR.getCode(), ErrorCode.EMAIL_ERROR.getMsg());
        }

        //TODO 应该防止恶意评论，具体实现搜一下
        commentService.addComment(comment);

        return Result.success(null);
    }

    //根据id删除评论
    @DeleteMapping("/delete/{id}")
    @RequireLogin(true)
    @LogAnnotation(module = "评论", operation = "删除")
    public Result deleteCommentById(@PathVariable("id") Long id) {
        commentService.deleteCommentById(id);

        return Result.success(null);
    }
}
