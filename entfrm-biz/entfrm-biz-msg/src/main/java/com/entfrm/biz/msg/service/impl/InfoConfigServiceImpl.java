package com.entfrm.biz.msg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.msg.mapper.InfoConfigMapper;
import com.entfrm.biz.msg.entity.InfoConfig;
import com.entfrm.biz.msg.service.InfoConfigService;

/**
 * @author entfrm
 * @date 2020-05-10 14:53:43
 * @description 消息配置Service业务层
 */
@Service
public class InfoConfigServiceImpl extends ServiceImpl<InfoConfigMapper, InfoConfig> implements InfoConfigService {
}
