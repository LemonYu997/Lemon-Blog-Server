package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lemonyu997.top.lemonapi.mapper.ArticleTagMapper;
import lemonyu997.top.lemonapi.mapper.TagMapper;
import lemonyu997.top.lemonapi.pojo.ArticleTag;
import lemonyu997.top.lemonapi.pojo.Tag;
import lemonyu997.top.lemonapi.service.TagService;
import lemonyu997.top.lemonapi.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    //根据标签id获取标签
    @Override
    public TagVO findTagById(Long tagId) {
        Tag tag = tagMapper.selectById(tagId);
        TagVO tagVO = copy(tag);

        return tagVO;
    }

    //查询全部标签
    @Override
    public List<Tag> findAllTag() {

        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<>());

        return tagList;
    }

    //获取全部标签，用于后台展示，需要显示文章数
    @Override
    public List<TagVO> findAllTagForAdmin() {
        List<TagVO> tagVOList = new ArrayList<>();
        List<Tag> tagList = findAllTag();

        TagVO tagVO;

        for (Tag tag : tagList) {
            tagVO = copy(tag);
            tagVOList.add(tagVO);
        }

        return tagVOList;
    }

    //添加新标签
    @Override
    @Transactional
    public void addTag(Tag tag) {
        //执行插入操作
        tagMapper.insert(tag);
    }

    //更新标签
    @Override
    @Transactional
    public void updateTag(Tag tag) {
        //更新标签表中的标签名即可
        tagMapper.updateById(tag);
    }

    //根据id删除标签
    @Override
    @Transactional
    public void deleteById(Long id) {
        //删除标签表中的数据
        tagMapper.deleteById(id);

        //删除中间表中的数据
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, id);

        articleTagMapper.delete(queryWrapper);
    }

    //将tag转换为tagVO
    private TagVO copy(Tag tag) {
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);

        Integer count = tagMapper.countArticleByTagId(tag.getId());
        tagVO.setArticleCounts(count);

        return tagVO;
    }
}
