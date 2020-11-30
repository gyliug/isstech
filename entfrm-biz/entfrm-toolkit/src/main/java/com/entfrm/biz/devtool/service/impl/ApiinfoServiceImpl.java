package com.entfrm.biz.devtool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entfrm.biz.devtool.entity.Apiinfo;
import com.entfrm.biz.devtool.mapper.ApiinfoMapper;
import com.entfrm.biz.devtool.service.ApiinfoService;
import org.springframework.stereotype.Service;

/**
 * @author entfrm
 * @date 2020-04-24 22:18:00
 * @description 接口Service业务层
 */
@Service
public class ApiinfoServiceImpl extends ServiceImpl<ApiinfoMapper, Apiinfo> implements ApiinfoService {
}
