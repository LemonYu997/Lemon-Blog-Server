package lemonyu997.top.lemonapi.service;

import lemonyu997.top.lemonapi.pojo.Recommend;

import java.util.List;

public interface RecommendService {
    //根据id获得推荐
    Recommend findRecommendById(Integer id);

    //获得推荐列表
    List<Recommend> findRecommendList();

    //添加推荐
    void addRecommend(Recommend recommend);

    //根据id删除推荐
    void deleteRecommendById(Integer id);

    //更新推荐
    void updateRecommend(Recommend recommend);
}
