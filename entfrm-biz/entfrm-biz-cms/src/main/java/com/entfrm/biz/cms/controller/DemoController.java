package com.entfrm.biz.cms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.cms.entity.Demo;
import com.entfrm.biz.cms.service.DemoService;
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
 * @date 2020-08-06 23:13:57
 * @description 示例Controller
 */
@Api("示例管理")
@RestController
@AllArgsConstructor
@RequestMapping("/cms/demo")
public class DemoController {

    private final DemoService demoService;

    private QueryWrapper<Demo> getQueryWrapper(Demo demo) {
        return new QueryWrapper<Demo>()
                .like(StrUtil.isNotBlank(demo.getName()), "name", demo.getName())
                .between(StrUtil.isNotBlank(demo.getBeginTime()) && StrUtil.isNotBlank(demo.getEndTime()), "create_time", demo.getBeginTime(), demo.getEndTime())
                .orderByDesc("create_time");
    }

    @ApiOperation("示例列表")
    @PreAuthorize("@ps.hasPerm('demo_view')")
    @GetMapping("/list")
    public R list(Page page, Demo demo) {
        IPage<Demo> demoPage = demoService.page(page, getQueryWrapper(demo));
        return R.ok(demoPage.getRecords(), demoPage.getTotal());
    }

    @ApiOperation("示例查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(demoService.getById(id));
    }

    @OperLog("示例新增")
    @ApiOperation("示例新增")
    @PreAuthorize("@ps.hasPerm('demo_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody Demo demo) {
        demoService.save(demo);
        return R.ok();
    }

    @OperLog("示例修改")
    @ApiOperation("示例修改")
    @PreAuthorize("@ps.hasPerm('demo_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody Demo demo) {
        demoService.updateById(demo);
        return R.ok();
    }


    @OperLog("示例删除")
    @ApiOperation("示例删除")
    @PreAuthorize("@ps.hasPerm('demo_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(demoService.removeByIds(Arrays.asList(id)));
    }


    @PreAuthorize("@ps.hasPerm('demo_export')")
    @GetMapping("/export")
    public R export(Demo demo) {
        List<Demo> list = demoService.list(getQueryWrapper(demo));
        ExcelUtil<Demo> util = new ExcelUtil<Demo>(Demo.class);
        return util.exportExcel(list, "示例数据");
    }
}
