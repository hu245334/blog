package org.example.blog.service;

import org.example.blog.pojo.Article;
import org.example.blog.pojo.PageBean;

public interface ArticleService {
    // 添加文章
    void add(Article article);

    // 条件分页列表查询
    PageBean<Article> list(Integer pageNo, Integer pageSize, Integer categoryId, String state);

    // 更新文章
    void update(Article article);

    // 删除文章
    void delete(Integer id);

    // 文章详情
    Article detail(Integer id);
}
