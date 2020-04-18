package com.entfrm.biz.cms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.core.base.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.entfrm.core.data.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author entfrm
 * @date 2020-03-31 22:57:28
 * @description 类别对象 Category
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cms_category")
public class Category extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableId
    private Integer id;

    /**
     * 类别名称
     */
    @Excel(name = "类别名称")
    private String name;

    /**
     * 父级编号
     */
    private Integer parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer sort;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", convertExp = "0=正常,1=停用")
    private String status;


    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Category> children = new ArrayList<>();
}
