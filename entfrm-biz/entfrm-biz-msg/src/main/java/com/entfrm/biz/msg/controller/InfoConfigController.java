package com.entfrm.biz.msg.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entfrm.biz.msg.entity.InfoConfig;
import com.entfrm.biz.msg.service.InfoConfigService;
import com.entfrm.base.api.R;
import com.entfrm.base.util.ExcelUtil;
import com.entfrm.log.annotation.OperLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author entfrm
 * @date 2020-05-10 14:53:43
 * @description 消息配置Controller
 */
@Api("消息配置管理")
@RestController
@AllArgsConstructor
@RequestMapping("/msg/infoConfig")
public class InfoConfigController {

    private final InfoConfigService infoConfigService;

    private QueryWrapper<InfoConfig> getQueryWrapper(InfoConfig infoConfig) {
        return new QueryWrapper<InfoConfig>()
                .like(StrUtil.isNotBlank(infoConfig.getName()), "name", infoConfig.getName())
                .eq(StrUtil.isNotBlank(infoConfig.getType()), "type", infoConfig.getType())
                .eq(StrUtil.isNotBlank(infoConfig.getStatus()), "status", infoConfig.getStatus())
                .orderByDesc("create_time");
    }

    @ApiOperation("消息配置列表")
    @PreAuthorize("@ps.hasPerm('infoConfig_view')")
    @GetMapping("/list")
    public R list(Page page, InfoConfig infoConfig) {
        IPage<InfoConfig> infoConfigPage = infoConfigService.page(page, getQueryWrapper(infoConfig));
        return R.ok(infoConfigPage.getRecords(), infoConfigPage.getTotal());
    }

    @ApiOperation("消息配置查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(infoConfigService.getById(id));
    }

    @OperLog("消息配置新增")
    @ApiOperation("消息配置新增")
    @PreAuthorize("@ps.hasPerm('infoConfig_add')")
    @PostMapping("/save")
    public R save(@Validated @RequestBody InfoConfig infoConfig) {
        infoConfigService.save(infoConfig);
        return R.ok();
    }

    @OperLog("消息配置修改")
    @ApiOperation("消息配置修改")
    @PreAuthorize("@ps.hasPerm('infoConfig_edit')")
    @PutMapping("/update")
    public R update(@Validated @RequestBody InfoConfig infoConfig) {
        infoConfigService.updateById(infoConfig);
        return R.ok();
    }

    @OperLog("消息配置删除")
    @ApiOperation("消息配置删除")
    @PreAuthorize("@ps.hasPerm('infoConfig_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(infoConfigService.removeByIds(Arrays.asList(id)));
    }

    @PreAuthorize("@ps.hasPerm('infoConfig_export')")
    @GetMapping("/export")
    public R export(InfoConfig infoConfig) {
        List<InfoConfig> list = infoConfigService.list(getQueryWrapper(infoConfig));
        ExcelUtil<InfoConfig> util = new ExcelUtil<InfoConfig>(InfoConfig.class);
        return util.exportExcel(list, "消息配置数据");
    }

    @OperLog("短信发送")
    @ApiOperation("短信发送")
    @GetMapping("/sendSms/{id}")
    public R sendSms(@PathVariable("id") Integer id) {
        InfoConfig infoConfig = infoConfigService.getById(id);
        if(infoConfig != null){
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", infoConfig.getAccessKeyId(), infoConfig.getAccessKeySecret());
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain(infoConfig.getHost());
            request.setVersion("1.1.0");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }

    @OperLog("邮件发送")
    @ApiOperation("邮件发送")
    @GetMapping("/sendEmail/{id}")
    public R sendEmail(@PathVariable("id") Integer id, @RequestParam String toEmail, @RequestParam String subject, @RequestParam String content) {
        InfoConfig infoConfig = infoConfigService.getById(id);
        if(infoConfig != null) {
            MailAccount account = new MailAccount();
            account.setHost(infoConfig.getHost());
            account.setPort(infoConfig.getPort());
            account.setAuth(true);
            account.setFrom(infoConfig.getFromBy());
            account.setUser(infoConfig.getUser());
            account.setPass(infoConfig.getPass());

            MailUtil.send(account, CollUtil.newArrayList(toEmail), subject, content, false);
        }
        return R.ok();
    }
}
