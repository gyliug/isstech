package com.entfrm.biz.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.msg.mapper.InfoTemplateMapper;
import com.entfrm.biz.msg.entity.InfoTemplate;
import com.entfrm.biz.msg.service.InfoTemplateService;

/**
 * @author entfrm
 * @date 2020-05-24 22:26:58
 * @description 消息模板Service业务层
 */
@Service
public class InfoTemplateServiceImpl extends ServiceImpl<InfoTemplateMapper, InfoTemplate> implements InfoTemplateService {
}
