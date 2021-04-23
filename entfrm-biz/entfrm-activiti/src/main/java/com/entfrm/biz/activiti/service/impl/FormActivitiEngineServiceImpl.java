package com.entfrm.biz.activiti.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entfrm.biz.activiti.dto.FormActivitiEngineDesignDto;
import com.entfrm.biz.activiti.entity.FormActivitiEngine;
import com.entfrm.biz.activiti.mapper.FormActivitiEngineMapper;
import com.entfrm.biz.activiti.service.FormActivitiEngineService;
import com.entfrm.security.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 工作流动态表单引擎业务实现层
 * </p>
 *
 * @Author: entfrm开发团队-王翔
 * @Date: 2021/4/19
 */
@Service
@AllArgsConstructor
public class FormActivitiEngineServiceImpl extends ServiceImpl<FormActivitiEngineMapper, FormActivitiEngine> implements FormActivitiEngineService {


    @Override
    public Boolean formActivitiEngineDesignUpdate(FormActivitiEngineDesignDto formActivitiEngineDesignDto) {

        return this.update(new FormActivitiEngine()
                        .setFormConf(JSONUtil.toJsonStr(formActivitiEngineDesignDto.getJsonMode().getFormConf()))
                        .setDrawingControls(JSONUtil.toJsonStr(formActivitiEngineDesignDto.getJsonMode().getDrawingControls()))
                        .setIdGlobal(formActivitiEngineDesignDto.getIdGlobal())
                        .setTreeNodeId(formActivitiEngineDesignDto.getTreeNodeId())
                        .setUpdateBy(SecurityUtil.getUser().getUsername()).setUpdateTime(new Date()),
                Wrappers.<FormActivitiEngine>lambdaUpdate().eq(FormActivitiEngine::getId,formActivitiEngineDesignDto.getDynamicFormId()));

    }

    @Override
    public FormActivitiEngine formActivitiEngineDesignSelect(Long id) {
        return baseMapper.findFormActivitiEngineDesignerData(id);
    }

    @Override
    public String fetchFormActivitiEngine(String code) {
        return baseMapper.findByCodeOne(code);
    }
}
