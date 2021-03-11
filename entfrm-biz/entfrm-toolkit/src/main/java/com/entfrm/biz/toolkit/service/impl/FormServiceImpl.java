package com.entfrm.biz.toolkit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.toolkit.mapper.FormMapper;
import com.entfrm.biz.toolkit.entity.Form;
import com.entfrm.biz.toolkit.service.FormService;

/**
 * @author entfrm
 * @date 2021-03-11 21:57:03
 * @description 表单管理Service业务层
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {
}
