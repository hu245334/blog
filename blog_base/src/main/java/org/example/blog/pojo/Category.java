package org.example.blog.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    @NotNull(groups = {Update.class})
    private Integer id;//主键ID
    @NotEmpty
    private String categoryName;//分类名称
    @NotEmpty
    private String categoryAlias;//分类别名
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    // 分组校验
    // 如果说某个校验项没有指定分组，则默认是 Default 分组
    // 分组之间可以继承，A extends B，A 分组就拥有 B 分组的所有校验规则
    public interface Add extends Default {
    }

    public interface Update extends Default {
    }
}
