package com.entfrm.biz.msg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.msg.entity.InfoTemplate;
import com.entfrm.biz.msg.service.InfoTemplateService;
import com.entfrm.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author entfrm
 * @date 2019-10-13 20:06:20
 * @description 消息模板Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/infoTemplate")
public class InfoTemplateController {

    private final InfoTemplateService infoTemplateService;
    private final RedisTemplate redisTemplate;

    private QueryWrapper<InfoTemplate> getQueryWrapper(InfoTemplate infoTemplate) {
        return new QueryWrapper<InfoTemplate>().orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('infoTemplate_view')")
    @GetMapping("/list")
    public R list(Page page, InfoTemplate infoTemplate) {
        IPage<InfoTemplate> infoTemplatePage = infoTemplateService.page(page, getQueryWrapper(infoTemplate));
        return R.ok(infoTemplatePage.getRecords(), infoTemplatePage.getTotal());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoTemplateService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('infoTemplate_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody InfoTemplate infoTemplate) {
        redisTemplate.opsForValue().set("tpl_" + infoTemplate.getTplKey(), infoTemplate.getTplContent());
        infoTemplateService.save(infoTemplate);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoTemplate_edit')")
    @PostMapping("/update")
    public R update(@Validated @RequestBody InfoTemplate infoTemplate) {
        redisTemplate.opsForValue().set("tpl_" + infoTemplate.getTplKey(), infoTemplate.getTplContent());
        infoTemplateService.updateById(infoTemplate);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoTemplate_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(infoTemplateService.removeByIds(Arrays.asList(id)));
    }

    /**
     * 用户状态修改
     */
    @PreAuthorize("@ps.hasPerm('infoTemplate_edit')")
    @GetMapping("/changeStatus")
    public R changeStatus(InfoTemplate infoTemplate) {
        infoTemplateService.updateById(infoTemplate);
        return R.ok();
    }
}
