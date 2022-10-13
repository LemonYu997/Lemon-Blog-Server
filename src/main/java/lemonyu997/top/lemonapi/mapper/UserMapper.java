package lemonyu997.top.lemonapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lemonyu997.top.lemonapi.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //根据用户id查询作者昵称
    String findNicknameById(Integer authorId);
}
