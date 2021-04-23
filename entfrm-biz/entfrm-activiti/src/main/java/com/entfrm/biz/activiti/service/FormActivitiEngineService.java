package com.entfrm.biz.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entfrm.biz.activiti.dto.FormActivitiEngineDesignDto;
import com.entfrm.biz.activiti.entity.FormActivitiEngine;

/**
 * <p>
 * 工作流动态表单业务接口
 * </p>
 *
 * @Author: entfrm开发团队-王翔
 * @Date: 2021/4/19
 */
public interface FormActivitiEngineService extends IService<FormActivitiEngine> {


    Boolean formActivitiEngineDesignUpdate(FormActivitiEngineDesignDto formActivitiEngineDesignDto);


    FormActivitiEngine formActivitiEngineDesignSelect(Long id);

    String fetchFormActivitiEngine(String code);

}
