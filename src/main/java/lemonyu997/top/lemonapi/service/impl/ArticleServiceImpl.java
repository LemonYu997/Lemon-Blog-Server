package lemonyu997.top.lemonapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lemonyu997.top.lemonapi.mapper.*;
import lemonyu997.top.lemonapi.pojo.*;
import lemonyu997.top.lemonapi.service.ArticleService;
import lemonyu997.top.lemonapi.thread.ThreadService;
import lemonyu997.top.lemonapi.utils.UserThreadLocal;
import lemonyu997.top.lemonapi.vo.ArchiveVO;
import lemonyu997.top.lemonapi.vo.ArticleVO;
import lemonyu997.top.lemonapi.vo.admin.ArticleAdminVO;
import lemonyu997.top.lemonapi.vo.params.ArticleParams;
import lemonyu997.top.lemonapi.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private CommentMapper commentMapper;

    //根据id查询文章详情
    @Override
    public ArticleVO findArticleDetailById(Long articleId) {
        //1、根据id查询文章
        Article article = articleMapper.selectById(articleId);
        //2、关联查询
        ArticleVO articleVO = copy(article, true, true, true, true);
        //3、查看完文章后，阅读数+1
        //TODO 理论上应该加个限制，只有在前台页面调用该接口才+1，现在后台调用也+1了
        //问题：查看文章之后，本应该返回数据，这时候做一个更新操作，更新时会在图书馆加写锁，会阻塞其他的读操作，性能比较低
        //由于更新操作会增加此接口（findArticleById）的耗时，可以做优化
        //如果一旦更新出了问题，不能影响查看文章的操作
        //使用线程池进行优化，可以把更新操作扔到线程池中去执行，和主线程不相关
        threadService.updateArticleViewCount(articleMapper, article);

        return articleVO;
    }

    //返回分页列表
    @Override
    public List<ArticleVO> listArticle(PageParams pageParams) {
        //封装分页查询参数，根据该条件进行查询，使用Mybatis-plus提供的分页对象
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        //自己实现分页查询的SQL语句，自定义查询条件
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());

        //返回查询到的结果
        List<Article> articleList = articleIPage.getRecords();
        //封装为返回对象，只需要标签和作者信息
        List<ArticleVO> articleVOList = copyList(articleList, true, false, false, true);

        return articleVOList;
    }

    //最新文章
    @Override
    public List<ArticleVO> newArticle(int limit) {
        //select id, title from article order by create_date desc limit 8
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //封装查询条件，根据创建时间排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        //查询对应的文章id和标题和时间
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateDate);
        //查询8条结果
        queryWrapper.last("limit " + limit);

        //执行查询操作
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        //封装为返回对象
        List<ArticleVO> articleVOList = copyList(articleList, false, false, false, false);

        return articleVOList;
    }

    //获取首页展示的文章归档列表信息
    @Override
    public List<ArchiveVO> getArchiveInfoList() {
        List<ArchiveVO> archiveVOList = articleMapper.getArchiveInfoList();

        return archiveVOList;
    }

    //根据发布时间查找对应的归档信息，主要是文章数量
    //TODO 这里需要重构，因为直接用2022年写死了
    @Override
    public ArchiveVO findArchiveInfoByTime(String year, String month) {
        ArchiveVO archiveVO = new ArchiveVO();

        Integer count = articleMapper.countArticleByTime(Integer.parseInt(year), Integer.parseInt(month));
//        System.out.println("*****************************" + count + "*************************************");

        archiveVO.setYear(year);
        archiveVO.setMonth(month);
        archiveVO.setCount(count);

        return archiveVO;
    }

    //添加文章操作，返回文章id
    @Override
    @Transactional  //使用事务
    public Map<String, String> addArticle(ArticleParams articleParams) {
        //要添加的文章对象
        Article article = new Article();
        //1、将传递的相关参数封装到文章对象中
        article.setFirstpicture(articleParams.getFirstpicture());
        article.setTitle(articleParams.getTitle());
        article.setSummary(articleParams.getSummary());
        article.setCategoryId(articleParams.getCategoryId());
        article.setWordCounts(articleParams.getWordCounts());
        article.setWeight(articleParams.getWeight());

        //2、封装初始化参数
        article.setCommentCounts(0);
        article.setViewCounts(0);
        article.setCreateDate(System.currentTimeMillis());

        //3、封装作者信息
        //获取当前登录用户
        User user = UserThreadLocal.get();
        article.setAuthorId(user.getId());

        //4、获取文章id
        //执行一次添加操作，id会自动回填
        articleMapper.insert(article);

        //5、封装正文内容
        ArticleBody articleBody = new ArticleBody();
        //获取插入操作后自动回填的文章id
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getContent());
        articleBody.setContentHtml("");
        articleBodyMapper.insert(articleBody);

        //6、封装标签
        List<String> tagsName = articleParams.getTagsName();
        for (String tagName : tagsName) {
            //根据标签名查询标签是否存在
            Tag tagByFind = tagMapper.findTagByTagName(tagName);
            if (tagByFind == null) {
                Tag tag = new Tag();
                //如果不存在，在标签表插入该标签
                tag.setTagName(tagName);
                tagMapper.insert(tag);
                //在中间表中插入数据
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            } else {
                //如果存在该标签，向中间表插入数据
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagByFind.getId());
                articleTagMapper.insert(articleTag);
            }
        }

        //7、将正文id更新到文章对象中
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        //8、返回文章id
        Map<String, String> articleIdMap = new HashMap<>();
        articleIdMap.put("id", article.getId().toString());

        return articleIdMap;
    }

    //根据id更新文章
    @Override
    @Transactional
    public Map<String, String> updateArticle(ArticleParams articleParams) {
        //即将更新的文章对象
        Article article = articleMapper.selectById(articleParams.getId());
        //1、将传递的相关参数封装到文章对象中
        article.setFirstpicture(articleParams.getFirstpicture());
        article.setTitle(articleParams.getTitle());
        article.setSummary(articleParams.getSummary());
        article.setCategoryId(articleParams.getCategoryId());
        article.setWordCounts(articleParams.getWordCounts());
        articleMapper.updateById(article);

        //置顶功能暂不考虑
//        article.setWeight(articleParams.getWeight());

        //2、更新正文内容
        ArticleBody articleBody = articleBodyMapper.selectById(article.getBodyId());
        articleBody.setContent(articleParams.getContent());
        articleBodyMapper.updateById(articleBody);
        System.out.println("----------------------------" + articleBody);

        //3、更新标签
        //先删除中间表中相关联的标签
        articleTagMapper.deleteByArticleId(article.getId());
        //重新添加标签
        List<String> tagsName = articleParams.getTagsName();
        for (String tagName : tagsName) {
            //根据标签名查询标签是否存在
            Tag tagByFind = tagMapper.findTagByTagName(tagName);
            if (tagByFind == null) {
                Tag tag = new Tag();
                //如果不存在，在标签表插入该标签
                tag.setTagName(tagName);
                tagMapper.insert(tag);
                //在中间表中插入数据
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            } else {
                //如果存在该标签，向中间表插入数据
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagByFind.getId());
                articleTagMapper.insert(articleTag);
            }
        }

        //4、返回文章id
        Map<String, String> articleIdMap = new HashMap<>();
        articleIdMap.put("id", article.getId().toString());

        return articleIdMap;
    }

    //获取所有文章，用于后台展示
    @Override
    public List<ArticleAdminVO> findAllArticleForAdmin() {
        //封装查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getFirstpicture, Article::getTitle, Article::getWordCounts, Article::getCreateDate, Article::getCategoryId, Article::getViewCounts, Article::getCommentCounts);
        queryWrapper.orderByDesc(Article::getCreateDate);

        //得到所有文章
        List<Article> articleList = articleMapper.selectList(queryWrapper);

        //前端需要的信息
        List<ArticleAdminVO> articleAdminVOList = new ArrayList<>();

        //生成列表
        for (Article article : articleList) {
            articleAdminVOList.add(copyForAdmin(article));
        }

        return articleAdminVOList;
    }

    //根据文章id删除文章
    @Override
    @Transactional
    public void deleteArticleById(Long id) {
        Article article = articleMapper.selectById(id);

        //1、删除文章表中的数据
        articleMapper.deleteById(id);
        //2、删除正文表中的数据
        articleBodyMapper.deleteById(article.getBodyId());
        //3、删除标签
        //先获取当前文章的所有标签
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        //删除标签中间表的数据
        articleTagMapper.deleteByArticleId(id);

        //查询该文章的标签如果是唯一的，则删除
        for (Tag tag : tags) {
            //如果当前标签的文章数为0，则删除该标签
            Integer count = tagMapper.countArticleByTagId(tag.getId());
//            System.out.println(tag + "------" + count);
            if (count == 0) {
                tagMapper.deleteById(tag);
            }
        }

        //4、删除评论
        commentMapper.deleteByArticleId(id);
    }

    //后台展示用的返回数据
    private ArticleAdminVO copyForAdmin(Article article) {
        ArticleAdminVO articleAdminVO = new ArticleAdminVO();
        BeanUtils.copyProperties(article, articleAdminVO);
        //跑在服务器的时候要+8小时
        articleAdminVO.setCreateDate(new DateTime(article.getCreateDate() + 8 * 60 * 60 * 1000).toString("yyyy-MM-dd HH:mm:ss"));

        //封装分类信息
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleAdminVO.setCategory(category);

        //封装标签信息
        List<Tag> tagList = tagMapper.findTagsByArticleId(article.getId());
        articleAdminVO.setTags(tagList);

        return articleAdminVO;
    }

    //根据不同情况，决定是否封装相应信息给返回类
    private ArticleVO copy(Article article, boolean isAuthor, boolean isBody, boolean isCategory, boolean isTag) {
        //实际返回的对象
        ArticleVO articleVO = new ArticleVO();

        //将相同属性封装到返回对象中
        BeanUtils.copyProperties(article, articleVO);
        //将Long类型时间戳转换为String字符串
        articleVO.setCreateDate(new DateTime(article.getCreateDate() + 8 * 60 * 60 * 1000).toString("yyyy-MM-dd HH:mm:ss"));

        //根据条件封装相应信息
        if (isAuthor) {
            //封装作者信息
            Integer authorId = article.getAuthorId();
            String author = userMapper.findNicknameById(authorId);
            articleVO.setAuthor(author);
        }

        if (isBody) {
            //封装正文信息，根据文章id查询
            String content = articleBodyMapper.findContentByArticleId(article.getId());
            articleVO.setContent(content);
        }

        if (isCategory) {
            //封装分类信息，根据分类id查询
            Category category = categoryMapper.selectById(article.getCategoryId());
            articleVO.setCategory(category);
        }

        if (isTag) {
            //封装标签信息
            //根据文章id查询标签列表
            List<Tag> tagList = tagMapper.findTagsByArticleId(article.getId());
            articleVO.setTags(tagList);
        }

        return articleVO;
    }

    //以集合形式获得返回封装好的文章对象
    private List<ArticleVO> copyList(List<Article> articleList, boolean isAuthor, boolean isBody, boolean isCategory, boolean isTag) {
        List<ArticleVO> articleVOList = new ArrayList<>();

        ArticleVO articleVO;

        for (Article article : articleList) {
            articleVO = copy(article, isAuthor, isBody, isCategory, isTag);
            articleVOList.add(articleVO);
        }

        return articleVOList;
    }
}
