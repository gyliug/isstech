package com.entfrm.biz.msg.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.msg.entity.InfoContent;
import com.entfrm.biz.msg.entity.InfoPush;
import com.entfrm.biz.msg.service.InfoContentService;
import com.entfrm.biz.msg.service.InfoPushService;
import com.entfrm.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;


/**
 * @author entfrm
 * @date 2019-08-25 22:46:40
 * @description 消息推送Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/infoPush")
public class InfoPushController {

    private final InfoPushService infoPushService;
    private final InfoContentService infoContentService;

    private QueryWrapper<InfoPush> getQueryWrapper(InfoPush infoPush) {
        return new QueryWrapper<InfoPush>().eq(StrUtil.isNotBlank(infoPush.getIsRead()), "is_read", infoPush.getIsRead()).orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('infoPush_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, InfoPush infoPush) {
        IPage<InfoPush> infoPushPage = infoPushService.page(page, getQueryWrapper(infoPush));
        for (InfoPush infoPush1 : infoPushPage.getRecords()) {
            InfoContent infoContent = infoContentService.getById(infoPush1.getMsginfoId());
            infoPush1.setTitle(infoContent.getTitle());
            infoPush1.setType(infoContent.getType());
            infoPush1.setContent(infoContent.getContent());
        }
        return R.ok(infoPushPage.getRecords(), infoPushPage.getTotal());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoPushService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('infoPush_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@Validated InfoPush infoPush) {
        if (infoPush.getId() != null) {
            infoPush.setIsRead("1");
            infoPush.setReadTime(new Date());
        }
        infoPushService.save(infoPush);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoPush_edit')")
    @PostMapping("/update")
    @ResponseBody
    public R update(@Validated InfoPush infoPush) {
        infoPushService.updateById(infoPush);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoPush_del')")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(infoPushService.removeByIds(Arrays.asList(id)));
    }
}
