package org.example.blog.mapper;

import org.apache.ibatis.annotations.*;
import org.example.blog.pojo.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time) " +
            "values(#{category.categoryName}, #{category.categoryAlias}, #{category.createUser}, #{category.createTime}, #{category.updateTime})")
    void add(@Param("category") Category category);

    @Select("select * from category where create_user = #{userId}")
    List<Category> list(@Param("userId") Integer id);

    @Select("select * from category where id = #{categoryId}")
    Category detail(@Param("categoryId") Long id);

    @Update("update category set " +
            "category_name = #{category.categoryName}, category_alias = #{category.categoryAlias}, update_time = #{category.updateTime} where id = #{category.id}")
    void update(@Param("category") Category category);

    @Delete("delete from category where id = #{categoryId}")
    void delete(@Param("categoryId") Long id);
}