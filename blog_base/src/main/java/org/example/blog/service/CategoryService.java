package org.example.blog.service;


import org.example.blog.pojo.Category;

import java.util.List;

public interface CategoryService {
    // 添加分类
    void add(Category category);

    // 列出所有分类
    List<Category> list();

    // 根据id查询分类
    Category detail(Long id);

    // 更新分类
    void update(Category category);

    // 删除分类
    void delete(Long id);
}
