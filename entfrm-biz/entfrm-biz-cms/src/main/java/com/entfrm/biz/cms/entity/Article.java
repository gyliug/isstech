package com.entfrm.biz.cms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.base.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.entfrm.data.entity.BaseEntity;

/**
 * @author entfrm
 * @date 2020-04-01 10:04:11
 * @description 文章对象 Article
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cms_article")
public class Article extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    @TableId
    private Integer id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 类别编号
     */
    private Integer categoryId;

    /**
     * 类别名称
     */
    @Excel(name = "类别名称")
    private String categoryName;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 摘要
     */
    @Excel(name = "摘要")
    private String summary;

    /**
     * 内容
     */
    private String contents;

    /**
     * 排序置顶
     */
    @Excel(name = "排序置顶")
    private Integer sort;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", convertExp = "0=正常,1=停用")
    private String status;


}
