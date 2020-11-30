package com.entfrm.biz.msg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.msg.entity.InfoContent;
import com.entfrm.biz.msg.entity.InfoPush;
import com.entfrm.biz.msg.service.InfoContentService;
import com.entfrm.biz.msg.service.InfoPushService;
import com.entfrm.base.api.R;
import com.entfrm.base.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * @author entfrm
 * @date 2019-08-25 22:45:45
 * @description 消息Controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/infoContent")
public class InfoContentController {

    private final InfoContentService infoContentService;
    private final InfoPushService infoPushService;

    private QueryWrapper<InfoContent> getQueryWrapper(InfoContent infoContent) {
        return new QueryWrapper<InfoContent>().orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('infoContent_view')")
    @GetMapping("/list")
    public R list(Page page, InfoContent infoContent) {
        IPage<InfoContent> infoContentPage = infoContentService.page(page, getQueryWrapper(infoContent));
        return R.ok(infoContentPage.getRecords(), infoContentPage.getTotal());
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoContentService.getById(id));
    }

    @PreAuthorize("@ps.hasPerm('infoContent_add')")
    @PostMapping("/save")
    @Transactional
    public R save(@Validated @RequestBody InfoContent infoContent) {
        infoContentService.save(infoContent);
        //推送消息
        String[] userIds = StrUtil.split(infoContent.getExtend(), ",");
        for (String userId : userIds) {
            InfoPush infoPush = new InfoPush();
            infoPush.setContentId(infoContent.getId());
            infoPush.setContentTitle(infoContent.getTitle());
            infoPush.setReceiveId(Integer.parseInt(userId));
            infoPush.setNoticeType(infoContent.getNoticeType());
            infoPush.setIsRead("0");
            infoPush.setCreateTime(new Date());
            infoPushService.save(infoPush);
        }
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoContent_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody InfoContent infoContent) {
        infoContentService.updateById(infoContent);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoContent_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(infoContentService.removeByIds(Arrays.asList(id)));
    }
}
