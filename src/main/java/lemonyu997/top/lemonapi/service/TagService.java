package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.pojo.Tag;
import lemonyu997.top.lemonapi.vo.TagVO;

import java.util.List;

public interface TagService {

    //根据标签id获得标签
    TagVO findTagById(Long tagId);

    //查询全部标签
    List<Tag> findAllTag();

    //获取全部标签，用于后台展示，需要显示文章数
    List<TagVO> findAllTagForAdmin();

    //添加新标签
    void addTag(Tag tag);

    //更新标签
    void updateTag(Tag tag);

    //根据id删除标签
    void deleteById(Long id);
}
