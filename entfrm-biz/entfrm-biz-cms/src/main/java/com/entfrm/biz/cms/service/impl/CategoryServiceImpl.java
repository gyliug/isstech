package com.entfrm.biz.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import com.entfrm.biz.cms.mapper.CategoryMapper;
import com.entfrm.biz.cms.entity.Category;
import com.entfrm.biz.cms.service.CategoryService;

/**
 * @author entfrm
 * @date 2020-03-31 22:57:28
 * @description 类别Service业务层
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    /**
     * 构建树
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<Category> buildTree(List<Category> list, int parentId) {
        List<Category> categoryList = new ArrayList<>();
        for (Iterator<Category> iterator = list.iterator(); iterator.hasNext(); ) {
            Category t = (Category) iterator.next();
            if (t.getParentId().intValue() == parentId) {
                recursion(list, t);
                categoryList.add(t);
            }
        }
        return categoryList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursion(List<Category> list, Category t) {
        // 得到子节点列表
        List<Category> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Category tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<Category> it = childList.iterator();
                while (it.hasNext()) {
                    Category n = (Category) it.next();
                    recursion(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Category> getChildList(List<Category> list, Category t) {
        List<Category> tlist = new ArrayList<Category>();
        Iterator<Category> it = list.iterator();
        while (it.hasNext()) {
            Category n = (Category) it.next();
            if (n.getParentId().intValue() == t.getId().intValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Category> list, Category t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
