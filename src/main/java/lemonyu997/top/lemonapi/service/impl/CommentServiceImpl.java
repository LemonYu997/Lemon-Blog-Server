package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lemonyu997.top.lemonapi.mapper.ArticleMapper;
import lemonyu997.top.lemonapi.mapper.CommentMapper;
import lemonyu997.top.lemonapi.pojo.Article;
import lemonyu997.top.lemonapi.pojo.Comment;
import lemonyu997.top.lemonapi.service.CommentService;
import lemonyu997.top.lemonapi.thread.ThreadService;
import lemonyu997.top.lemonapi.vo.CommentVO;
import lemonyu997.top.lemonapi.vo.admin.CommentAdminVO;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ThreadService threadService;

    //根据评论id查询评论
    @Override
    public Comment findCommentById(Long id) {
        Comment comment = commentMapper.selectById(id);

        return comment;
    }

    //根据文章id查询评论列表
    @Override
    public List<CommentVO> findCommentListByArticleId(Long articleId) {
        //封装查询条件，得到文章id的评论列表
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        //根据时间倒排
        queryWrapper.orderByDesc(Comment::getCreateDate);

        List<Comment> commentList = commentMapper.selectList(queryWrapper);

        List<CommentVO> commentVOList = copyList(commentList);

        return commentVOList;
    }

    //获取所有评论，用在后台操作
    @Override
    public List<CommentAdminVO> findAllCommentList() {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Comment::getCreateDate);

        List<Comment> commentList = commentMapper.selectList(queryWrapper);

        List<CommentAdminVO> commentAdminVOList = new ArrayList<>();
        CommentAdminVO commentAdminVO;

        for (Comment comment : commentList) {
            commentAdminVO = copyForAdmin(comment);
            commentAdminVOList.add(commentAdminVO);
        }

        return commentAdminVOList;
    }

    //发布评论
    //TODO 最好是把评论放在MangoDB中
    @Override
    @Transactional
    public void addComment(Comment comment) {
        //TODO 暂不实现层叠评论
        comment.setParentCommentId(0L);
        comment.setLevel(0);

        //设置发布时间
        comment.setCreateDate(System.currentTimeMillis());

        //添加评论
        commentMapper.insert(comment);

        //用多线程更新文章的评论数，实现读写分离
        //当前文章
        Article article = articleMapper.selectById(comment.getArticleId());

        //更新文章评论数
        threadService.updateArticleCommentCount(articleMapper, article);
    }

    //根据id删除评论
    @Override
    @Transactional
    public void deleteCommentById(Long id) {
        //获取评论对应的文章
        Comment comment = findCommentById(id);
        Article article = articleMapper.selectById(comment.getArticleId());

        //从评论表中删除评论
        commentMapper.deleteById(id);

        //更新文章评论数
        threadService.updateArticleCommentCountForDelete(articleMapper, article);
    }

    //以集合形式返回VO对象
    private List<CommentVO> copyList(List<Comment> commentList) {
        List<CommentVO> commentVOList = new ArrayList<>();

        CommentVO commentVO;

        for (Comment comment : commentList) {
            commentVO = copy(comment);
            commentVOList.add(commentVO);
        }

        return commentVOList;
    }


    //将数据库的评论对象封装为前台展示的VO对象
    private CommentVO copy(Comment comment) {
        CommentVO commentVO = new CommentVO();

        BeanUtils.copyProperties(comment, commentVO);
        //将Long类型时间戳转换为String字符串
        commentVO.setCreateDate(new DateTime(comment.getCreateDate() + 8 * 60 * 60 * 1000).toString("yyyy-MM-dd HH:mm:ss"));

        return commentVO;
    }

    //将数据库的评论对象封装为后台展示的VO对象
    private CommentAdminVO copyForAdmin(Comment comment) {
        CommentAdminVO commentAdminVO = new CommentAdminVO();

        BeanUtils.copyProperties(comment, commentAdminVO);
        //将Long类型时间戳转换为String字符串
        commentAdminVO.setCreateDate(new DateTime(comment.getCreateDate() + 8 * 60 * 60 * 1000).toString("yyyy-MM-dd HH:mm:ss"));

        //获取对应文章的标题
        Article article = articleMapper.selectById(commentAdminVO.getArticleId());

        if (article == null) {
            commentAdminVO.setArticleTitle("没有找到对应文章！请删除！");
        } else {
            commentAdminVO.setArticleTitle(article.getTitle());
        }

        return commentAdminVO;
    }

}
