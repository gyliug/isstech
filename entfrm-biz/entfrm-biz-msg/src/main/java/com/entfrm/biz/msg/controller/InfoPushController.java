package com.entfrm.biz.msg.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.msg.entity.InfoContent;
import com.entfrm.biz.msg.entity.InfoPush;
import com.entfrm.biz.msg.service.InfoContentService;
import com.entfrm.biz.msg.service.InfoPushService;
import com.entfrm.base.api.R;
import com.entfrm.security.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
        return new QueryWrapper<InfoPush>().eq(!StrUtil.isBlankIfStr(infoPush.getReceiveId()),"receive_id", infoPush.getReceiveId())
                .eq(StrUtil.isNotBlank(infoPush.getIsRead()), "is_read", infoPush.getIsRead()).orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('infoPush_view')")
    @GetMapping("/list")
    public R list(Page page, InfoPush infoPush) {
        IPage<InfoPush> infoPushPage = infoPushService.page(page, getQueryWrapper(infoPush));
        for (InfoPush infoPush1 : infoPushPage.getRecords()) {
            InfoContent infoContent = infoContentService.getById(infoPush1.getContentId());
            infoPush1.setContentTitle(infoContent.getTitle());
            infoPush1.setType(infoContent.getType());
            infoPush1.setContent(infoContent.getContent());
        }
        return R.ok(infoPushPage.getRecords(), infoPushPage.getTotal());
    }

    @GetMapping("/msgList")
    public R msgList(Page page, InfoPush infoPush) {
        infoPush.setReceiveId(SecurityUtil.getUser().getId());
        IPage<InfoPush> infoPushPage = infoPushService.page(page, getQueryWrapper(infoPush));
        for (InfoPush infoPush1 : infoPushPage.getRecords()) {
            InfoContent infoContent = infoContentService.getById(infoPush1.getContentId());
            infoPush1.setContentTitle(infoContent.getTitle());
            infoPush1.setType(infoContent.getType());
            infoPush1.setContent(infoContent.getContent());

            //更新状态为已读
            infoPush1.setIsRead("1");
            infoPush1.setReadTime(new Date());
            infoPushService.updateById(infoPush1);
        }
        return R.ok(infoPushPage.getRecords(), infoPushPage.getTotal());
    }

    @GetMapping("/msgInfo")
    public R msgInfo() {
        Map<String, Object> map = new HashMap<>();
        InfoPush infoPush = infoPushService.getOne(new QueryWrapper<InfoPush>().eq("receive_id", SecurityUtil.getUser().getId()).orderByDesc("create_time").last("limit 1"));
        if(infoPush != null){
            int count = infoPushService.count(new QueryWrapper<InfoPush>().eq("is_read", "0").eq("receive_id", SecurityUtil.getUser().getId()));
            map.put("count", count);
            map.put("info", infoPush.getContentTitle());
            map.put("createTime", infoPush.getCreateTime());
        }else {
            map.put("count", 0);
            map.put("info", "暂无");
            map.put("createTime", new Date());
        }
        return R.ok(map);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoPushService.getById(id));
    }


    @PreAuthorize("@ps.hasPerm('infoPush_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody InfoPush infoPush) {
        if (infoPush.getId() != null) {
            infoPush.setIsRead("1");
            infoPush.setReadTime(new Date());
        }
        infoPushService.save(infoPush);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoPush_edit')")
    @PostMapping("/update")
    public R update(@Validated @RequestBody InfoPush infoPush) {
        infoPushService.updateById(infoPush);
        return R.ok();
    }

    @PreAuthorize("@ps.hasPerm('infoPush_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(infoPushService.removeByIds(Arrays.asList(id)));
    }
}
