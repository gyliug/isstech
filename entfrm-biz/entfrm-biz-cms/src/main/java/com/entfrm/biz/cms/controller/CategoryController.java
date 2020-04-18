package com.entfrm.biz.cms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.cms.entity.Category;
import com.entfrm.biz.cms.service.CategoryService;
import com.entfrm.core.log.annotation.OperLog;
import org.springframework.security.access.prepost.PreAuthorize;
import com.entfrm.core.base.api.R;
import com.entfrm.core.base.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;

/**
 * @author entfrm
 * @date 2020-03-31 22:57:28
 * @description 类别Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/category")
public class CategoryController {

    private final CategoryService categoryService;

    private QueryWrapper<Category> getQueryWrapper(Category category) {
        return new QueryWrapper<Category>().like(StrUtil.isNotBlank(category.getName()), "name", category.getName()).orderByDesc("create_time")
                .eq(StrUtil.isNotBlank(category.getStatus()), "status", category.getStatus())
                .between(StrUtil.isNotBlank(category.getBeginTime()) && StrUtil.isNotBlank(category.getEndTime()), "create_time", category.getBeginTime(), category.getEndTime());
    }

    /**
     * 查询类别树列表
     */
    @PreAuthorize("@ps.hasPerm('category_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Category category) {
        List<Category> categoryList = categoryService.list(getQueryWrapper(category));

        return R.ok(categoryList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(categoryService.getById(id));
    }

    @OperLog("类别新增")
    @PreAuthorize("@ps.hasPerm('category_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@Validated @RequestBody Category category) {
        Category info = null;
        if (0 != category.getParentId()) {
            info = categoryService.getById(category.getParentId());
            category.setAncestors(info.getAncestors() + "," + category.getParentId());
        } else {
            category.setAncestors("0");
        }
        categoryService.save(category);
        return R.ok();
    }

    @OperLog("类别修改")
    @PreAuthorize("@ps.hasPerm('category_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@Validated @RequestBody Category category) {
        Category info = null;
        if (0 != category.getParentId()) {
            info = categoryService.getById(category.getParentId());
            category.setAncestors(info.getAncestors() + "," + category.getParentId());
        } else {
            category.setAncestors("0");
        }
        categoryService.updateById(category);
        return R.ok();
    }


    @OperLog("类别删除")
    @PreAuthorize("@ps.hasPerm('category_del')")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable("id") Integer id) {
        return R.ok(categoryService.removeById(id));
    }


    @GetMapping("/categoryTree")
    @ResponseBody
    public R categoryTree() {
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().orderByAsc("sort"));
        return R.ok(categoryService.buildTree(categoryList, 0));
    }

    @PreAuthorize("@ps.hasPerm('category_export')")
    @PostMapping("/export")
    @ResponseBody
    public R export(Category category) {
        List<Category> list = categoryService.list(getQueryWrapper(category));
        ExcelUtil<Category> util = new ExcelUtil<Category>(Category.class);
        return util.exportExcel(list, "类别数据");
    }
}
