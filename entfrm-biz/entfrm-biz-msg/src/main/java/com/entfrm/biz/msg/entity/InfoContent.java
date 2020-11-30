package com.entfrm.biz.msg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.base.annotation.Excel;
import com.entfrm.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author entfrm
 * @date 2020-05-23 12:14:03
 * @description 消息内容对象 InfoContent
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_info_content")
public class InfoContent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    private Integer id;

    /**
     * 消息标题
     */
    @Excel(name = "消息标题")
    private String title;

    /**
     * 消息类型
     */
    @Excel(name = "消息类型")
    private String type;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String files;

    /**
     * 连接
     */
    @Excel(name = "连接")
    private String url;

    /**
     * 归属机构
     */
    @Excel(name = "归属机构")
    private String deptId;

    /**
     * 通知类型
     */
    private String noticeType;

    /**
     * 通知类型数组
     */
    @TableField(exist = false)
    private String[] noticeTypes;


    /**
     * 扩展字段
     */
    @Excel(name = "扩展字段")
    private String extend;

    /**
     * 状态
     */
    private String status;

}
