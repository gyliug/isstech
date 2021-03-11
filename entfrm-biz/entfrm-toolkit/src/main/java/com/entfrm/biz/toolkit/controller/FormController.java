package com.entfrm.biz.toolkit.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.toolkit.entity.Form;
import com.entfrm.biz.toolkit.service.FormService;
import com.entfrm.log.annotation.OperLog;
import org.springframework.security.access.prepost.PreAuthorize;
import com.entfrm.base.api.R;
import com.entfrm.base.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Arrays;

/**
 * @author entfrm
 * @date 2021-03-11 21:57:03
 * @description 表单管理Controller
 */
@Api("表单管理管理")
@RestController
@AllArgsConstructor
@RequestMapping("/toolkit/form")
public class FormController {

    private final FormService formService;

    private QueryWrapper<Form> getQueryWrapper(Form form) {
        return new QueryWrapper<Form>()
                .like(StrUtil.isNotBlank(form.getName()), "name", form.getName())
                .eq(StrUtil.isNotBlank(form.getType()), "type", form.getType())
                .like(StrUtil.isNotBlank(form.getTableName()), "table_name", form.getTableName())
                .between(StrUtil.isNotBlank(form.getBeginTime()) && StrUtil.isNotBlank(form.getEndTime()), "create_time", form.getBeginTime(), form.getEndTime())
                .orderByDesc("id");
    }

    @ApiOperation("表单管理列表")
    @PreAuthorize("@ps.hasPerm('form_view')")
    @GetMapping("/list")
    public R list(Page page, Form form) {
        IPage<Form> formPage = formService.page(page, getQueryWrapper(form));
        return R.ok(formPage.getRecords(), formPage.getTotal());
    }

    @ApiOperation("表单管理查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(formService.getById(id));
    }

    @OperLog("表单管理新增")
    @ApiOperation("表单管理新增")
    @PreAuthorize("@ps.hasPerm('form_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody Form form) {
        formService.save(form);
        return R.ok();
    }

    @OperLog("表单管理修改")
    @ApiOperation("表单管理修改")
    @PreAuthorize("@ps.hasPerm('form_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody Form form) {
        formService.updateById(form);
        return R.ok();
    }


    @OperLog("表单管理删除")
    @ApiOperation("表单管理删除")
    @PreAuthorize("@ps.hasPerm('form_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(formService.removeByIds(Arrays.asList(id)));
    }


    @PreAuthorize("@ps.hasPerm('form_export')")
    @GetMapping("/export")
    public R export(Form form) {
        List<Form> list = formService.list(getQueryWrapper(form));
        ExcelUtil<Form> util = new ExcelUtil<Form>(Form.class);
        return util.exportExcel(list, "表单管理数据");
    }
}
