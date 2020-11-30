package com.entfrm.biz.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.cms.mapper.DemoMapper;
import com.entfrm.biz.cms.entity.Demo;
import com.entfrm.biz.cms.service.DemoService;

/**
 * @author entfrm
 * @date 2020-08-06 23:13:57
 * @description 示例Service业务层
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {
}
