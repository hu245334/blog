package org.example.blog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.blog.mapper.ArticleMapper;
import org.example.blog.pojo.Article;
import org.example.blog.pojo.PageBean;
import org.example.blog.service.ArticleService;
import org.example.blog.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);
        System.out.println(article.getCreateUser());
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNo, Integer pageSize, Integer categoryId, String state) {
        // 1. 创建PageBean对象
        PageBean<Article> pageBean = new PageBean<>();
        // 2. 开启分页查询 PageHelper
        PageHelper.startPage(pageNo, pageSize);

        // 3. 调用Mapper查询列表
        //  3.1 获取当前登录用户的id(从ThreadLocal中获取)
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        List<Article> articles = articleMapper.list(userId, categoryId, state);
        // Page中提供了方法 可以获取PageHelper中的分页数据
        Page<Article> page = (Page<Article>) articles;

        // 4. 封装PageBean对象
        pageBean.setTotal((int) page.getTotal());
        pageBean.setData(page.getResult());
        return pageBean;
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }

    @Override
    public Article detail(Integer id) {
        return articleMapper.detail(id);
    }
}
