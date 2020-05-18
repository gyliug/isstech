package com.entfrm.biz.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.cms.entity.Article;
import com.entfrm.biz.cms.entity.Category;
import com.entfrm.biz.cms.service.ArticleService;
import com.entfrm.biz.cms.service.CategoryService;
import com.entfrm.biz.cms.vo.CategoryVo;
import com.entfrm.core.base.util.StrUtil;
import com.entfrm.core.log.annotation.OperLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import com.entfrm.core.base.api.R;
import com.entfrm.core.base.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author entfrm
 * @date 2020-04-01 10:04:11
 * @description 文章Controller
 */
@Api("文章管理")
@RestController
@AllArgsConstructor
@RequestMapping("/cms/article")
public class ArticleController {

    private final CategoryService categoryService;
    private final ArticleService articleService;

    private QueryWrapper<Article> getQueryWrapper(Article article) {
        return new QueryWrapper<Article>()
                .eq(StrUtil.isNotBlank(article.getTitle()), "title", article.getTitle())
                .eq(!StrUtil.isEmptyIfStr(article.getCategoryId()), "category_id", article.getCategoryId())
                .eq(StrUtil.isNotBlank(article.getStatus()), "status", article.getStatus()).orderByAsc("sort");
    }

    @ApiOperation("文章列表")
    @PreAuthorize("@ps.hasPerm('article_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, Article article) {
        IPage<Article> articlePage = articleService.page(page, getQueryWrapper(article));
        return R.ok(articlePage.getRecords(), articlePage.getTotal());
    }

    @ApiOperation("文章查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(articleService.getById(id));
    }

    @OperLog("文章新增")
    @ApiOperation("文章新增")
    @PreAuthorize("@ps.hasPerm('article_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@Validated @RequestBody Article article) {
        articleService.save(article);
        return R.ok();
    }

    @OperLog("文章修改")
    @ApiOperation("文章修改")
    @PreAuthorize("@ps.hasPerm('article_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@Validated @RequestBody Article article) {
        articleService.updateById(article);
        return R.ok();
    }

    @OperLog("文章删除")
    @ApiOperation("文章删除")
    @PreAuthorize("@ps.hasPerm('article_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(articleService.removeByIds(Arrays.asList(id)));
    }

    @PreAuthorize("@ps.hasPerm('article_export')")
    @PostMapping("/export")
    @ResponseBody
    public R export(Article article) {
        List<Article> list = articleService.list(getQueryWrapper(article));
        ExcelUtil<Article> util = new ExcelUtil<Article>(Article.class);
        return util.exportExcel(list, "文章数据");
    }


    @GetMapping("/doc/categoryList")
    @ResponseBody
    public R categoryList() {
        List<CategoryVo> voList = new ArrayList<>();
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().eq("status", "0").orderByAsc("sort"));
        categoryList.stream().forEach(category -> {
            CategoryVo categoryVo = CategoryVo.builder().build();
            categoryVo.setId(StrUtil.genNewId(category.getId()));
            if(category.getParentId() != 0){
                categoryVo.setParentId(StrUtil.genNewId(category.getParentId()));
            }else {
                categoryVo.setParentId(category.getParentId());
            }
            categoryVo.setName(category.getName());
            categoryVo.setCNode(false);
            voList.add(categoryVo);
            articleService.list(new QueryWrapper<Article>().eq("category_id", category.getId()).eq("status", "0").orderByDesc("sort")).stream().forEach(article -> {
                voList.add(CategoryVo.builder().id(article.getId()).parentId(StrUtil.genNewId(category.getId()))
                        .name(article.getTitle()).cNode(true).build());
            });
        });
        return R.ok(voList);
    }

    @GetMapping("/doc/{id}")
    public R getDocById(@PathVariable("id") Integer id) {
        return R.ok(articleService.getById(id));
    }
}
