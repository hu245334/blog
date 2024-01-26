package org.example.blog.controller;

import com.auth0.jwt.JWT;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blog.pojo.Article;
import org.example.blog.pojo.PageBean;
import org.example.blog.pojo.Result;
import org.example.blog.service.ArticleService;
import org.example.blog.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNo,
                                          Integer pageSize,
                                          @RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String state) {
        PageBean<Article> pageBean = articleService.list(pageNo, pageSize, categoryId, state);
        return Result.success(pageBean);
    }

    @GetMapping("/detail")
    public Result<Article> detail(@RequestParam Integer id) {
        Article article = articleService.detail(id);
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        articleService.delete(id);
        return Result.success();
    }


}
