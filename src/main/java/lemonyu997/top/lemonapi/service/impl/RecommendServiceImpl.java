package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lemonyu997.top.lemonapi.mapper.RecommendMapper;
import lemonyu997.top.lemonapi.pojo.Recommend;
import lemonyu997.top.lemonapi.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;

    //根据id获得推荐
    @Override
    public Recommend findRecommendById(Integer id) {
        Recommend recommend = recommendMapper.selectById(id);

        return recommend;
    }

    //获得推荐列表
    @Override
    public List<Recommend> findRecommendList() {
        //根据时间正向排序
        LambdaQueryWrapper<Recommend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Recommend::getCreateDate);

        List<Recommend> recommendList = recommendMapper.selectList(queryWrapper);

        return recommendList;
    }

    //添加推荐
    @Override
    @Transactional
    public void addRecommend(Recommend recommend) {
        //设置创建时间
        recommend.setCreateDate(System.currentTimeMillis());

        //执行添加操作
        recommendMapper.insert(recommend);
    }

    //更新推荐
    @Override
    @Transactional
    public void updateRecommend(Recommend recommend) {
        recommendMapper.updateById(recommend);
    }

    //根据id删除推荐
    @Override
    @Transactional
    public void deleteRecommendById(Integer id) {
        recommendMapper.deleteById(id);
    }


}
