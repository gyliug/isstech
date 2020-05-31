package com.entfrm.biz.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.msg.mapper.InfoPushMapper;
import com.entfrm.biz.msg.entity.InfoPush;
import com.entfrm.biz.msg.service.InfoPushService;

/**
 * @author entfrm
 * @date 2020-05-24 22:26:59
 * @description 消息推送Service业务层
 */
@Service
public class InfoPushServiceImpl extends ServiceImpl<InfoPushMapper, InfoPush> implements InfoPushService {
}
