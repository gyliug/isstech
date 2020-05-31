package com.entfrm.biz.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.msg.mapper.InfoContentMapper;
import com.entfrm.biz.msg.entity.InfoContent;
import com.entfrm.biz.msg.service.InfoContentService;

/**
 * @author entfrm
 * @date 2020-05-23 12:14:03
 * @description 消息内容Service业务层
 */
@Service
public class InfoContentServiceImpl extends ServiceImpl<InfoContentMapper, InfoContent> implements InfoContentService {
}
