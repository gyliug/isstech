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
 * @date 2020-08-06 23:13:57
 *
 * @description 示例对象 Demo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cms_demo")
public class Demo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    @TableId
    private Integer id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;


}
