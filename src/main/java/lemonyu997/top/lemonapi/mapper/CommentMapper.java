package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lemonyu997.top.lemonapi.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    //删除评论
    void deleteByArticleId(Long id);
}
