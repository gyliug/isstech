package com.entfrm.biz.msg.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.core.metadata.IPage;
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import com.entfrm.biz.msg.entity.InfoContent;
import com.entfrm.biz.msg.service.InfoContentService;
import com.entfrm.core.log.annotation.OperLog;
import org.springframework.security.access.prepost.PreAuthorize;
import com.entfrm.core.base.api.R;
import com.entfrm.core.base.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Arrays;

/**
 * @author entfrm
 * @date 2020-05-23 12:14:03
 *
 * @description 消息内容Controller
 */
@Api("消息内容管理")
@RestController
@AllArgsConstructor
@RequestMapping("/msg/infoContent")
public class InfoContentController {

    private final InfoContentService infoContentService;

    private QueryWrapper<InfoContent> getQueryWrapper(InfoContent infoContent) {
            return new QueryWrapper<InfoContent>()
                                                            .eq(StrUtil.isNotBlank(infoContent.getTitle()), "title", infoContent.getTitle())
                                                    .eq(StrUtil.isNotBlank(infoContent.getType()), "type", infoContent.getType())
                                                    .eq(StrUtil.isNotBlank(infoContent.getContent()), "content", infoContent.getContent())
                                                    .eq(StrUtil.isNotBlank(infoContent.getFiles()), "files", infoContent.getFiles())
                                                    .eq(StrUtil.isNotBlank(infoContent.getUrl()), "url", infoContent.getUrl())
                                                    .eq(StrUtil.isNotBlank(infoContent.getDeptId()), "dept_id", infoContent.getDeptId())
                                                    .eq(StrUtil.isNotBlank(infoContent.getExtend()), "extend", infoContent.getExtend())
                                                                                                                                                    .orderByDesc("create_time");
    }

            @ApiOperation("消息内容列表")
        @PreAuthorize("@ps.hasPerm('infoContent_view')")
        @GetMapping("/list")
        public R list(Page page, InfoContent infoContent) {
            IPage<InfoContent> infoContentPage = infoContentService.page(page, getQueryWrapper(infoContent));
            return R.ok(infoContentPage.getRecords(), infoContentPage.getTotal());
        }
    
    @ApiOperation("消息内容查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoContentService.getById(id));
    }

    @OperLog("消息内容新增")
    @ApiOperation("消息内容新增")
    @PreAuthorize("@ps.hasPerm('infoContent_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody InfoContent infoContent) {
            infoContentService.save(infoContent);
        return R.ok();
    }

    @OperLog("消息内容修改")
    @ApiOperation("消息内容修改")
    @PreAuthorize("@ps.hasPerm('infoContent_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody InfoContent infoContent) {
            infoContentService.updateById(infoContent);
        return R.ok();
    }


    @OperLog("消息内容删除")
    @ApiOperation("消息内容删除")
    @PreAuthorize("@ps.hasPerm('infoContent_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer id) {
        return R.ok(infoContentService.removeById(id));
    }


    
    @PreAuthorize("@ps.hasPerm('infoContent_export')")
    @GetMapping("/export")
    public R export(InfoContent infoContent) {
        List<InfoContent> list = infoContentService.list(getQueryWrapper(infoContent));
        ExcelUtil<InfoContent> util = new ExcelUtil<InfoContent>(InfoContent. class);
        return util.exportExcel(list, "消息内容数据");
    }
}
