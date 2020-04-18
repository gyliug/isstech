package com.entfrm.biz.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entfrm.biz.cms.entity.Category;

import java.util.List;

/**
 * @author entfrm
 * @date 2020-03-31 22:57:28
 * @description 类别Service接口
 */
public interface CategoryService extends IService<Category> {
    /**
     * 构建树
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<Category> buildTree(List<Category> list, int parentId);
}
