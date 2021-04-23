package com.entfrm.biz.activiti.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.base.api.R;
import com.entfrm.biz.activiti.dto.FormActivitiEngineDesignDto;
import com.entfrm.biz.activiti.entity.FormActivitiEngine;
import com.entfrm.biz.activiti.service.FormActivitiEngineService;
import com.entfrm.security.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 工作流动态表单控制层
 * </p>
 *
 * @Author: entfrm开发团队-王翔
 * @Date: 2021/4/19
 */
@RestController
@RequestMapping("/activiti/FormActivitiEngine")
@AllArgsConstructor
public class FormActivitiEngineController {


    private final FormActivitiEngineService formActivitiEngineService;


    private QueryWrapper<FormActivitiEngine> getQueryWrapper(FormActivitiEngine formActivitiEngine) {
        return new QueryWrapper<FormActivitiEngine>()
                .like(StrUtil.isNotBlank(formActivitiEngine.getCode()),"code", formActivitiEngine.getCode())
                .like(StrUtil.isNotBlank(formActivitiEngine.getName()), "name", formActivitiEngine.getName())
                .between(StrUtil.isNotBlank(formActivitiEngine.getBeginTime()) && StrUtil.isNotBlank(formActivitiEngine.getEndTime()), "create_time", formActivitiEngine.getBeginTime(), formActivitiEngine.getEndTime());
    }


    @GetMapping("/list")
    public R list(Page page, FormActivitiEngine formExtend) {
        IPage<FormActivitiEngine> roleIPage = formActivitiEngineService.page(page, getQueryWrapper(formExtend));
        return R.ok(roleIPage.getRecords(), roleIPage.getTotal());
    }


    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(formActivitiEngineService.getById(id));
    }


    @GetMapping("fetchFormActivitiEngine/{code}")
    public R fetchFormActivitiEngine(@PathVariable("code") String code) {
        return R.ok(formActivitiEngineService.fetchFormActivitiEngine(code));
    }


    @PostMapping("/save")
    public R save(@RequestBody FormActivitiEngine formActivitiEngine) {
        if(formActivitiEngineService.list(new QueryWrapper<FormActivitiEngine>()
                .eq(StrUtil.isNotBlank(formActivitiEngine.getCode()),"code", formActivitiEngine.getCode())).size()==0){
            formActivitiEngine.setCreateBy(SecurityUtil.getUser().getUsername());
            formActivitiEngine.setCreateTime(new Date());
            formActivitiEngineService.save(formActivitiEngine);
        }else{
            return R.error("编码重复!");
        }
        return R.ok();
    }


    @PutMapping("/update")
    public R update(@RequestBody FormActivitiEngine formActivitiEngine) {
        formActivitiEngine.setUpdateBy(SecurityUtil.getUser().getUsername());
        formActivitiEngine.setUpdateTime(new Date());
        formActivitiEngineService.update(formActivitiEngine,new UpdateWrapper<FormActivitiEngine>().eq("id", formActivitiEngine.getId()));
        return R.ok();
    }


    @DeleteMapping("/remove/{ids}")
    public R remove(@PathVariable Integer[] ids) {
        formActivitiEngineService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }


    @PutMapping("design/update")
    public R save(@RequestBody FormActivitiEngineDesignDto formActivitiEngineDesignDto) {
        formActivitiEngineService.formActivitiEngineDesignUpdate(formActivitiEngineDesignDto);
        return R.ok();
    }


    @GetMapping("design/select/{id}")
    public R select(@PathVariable Long id) {
        return R.ok(formActivitiEngineService.formActivitiEngineDesignSelect(id));
    }


}
