package com.entfrm.biz.cms.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yong
 * @date 2020/4/2
 * @description 类别vo
 */
@Data
@Builder
public class CategoryVo implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 父级编号
     */
    private Integer parentId;

    /**
     * 是不是子节点
     */
    private boolean cNode;

    /**
     * 子级列表
     */
    private List<CategoryVo> children;

}
