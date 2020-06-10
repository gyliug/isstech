package com.entfrm.biz.msg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author entfrm
 * @date 2019-08-25 22:46:40
 *
 * @description 消息推送对象 InfoPush
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_info_push")
public class InfoPush implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId
    private Integer id;

    /** 消息类型 */
    @TableField(exist = false)
    private String type;

    /** 内容 */
    @TableField(exist = false)
    private String content;

    /** 消息编号 */
    private Integer contentId;

    /** 消息标题 */
    private String contentTitle;

    /** 接受人 */
    private Integer receiveId;

    /** 通知类型 */
    private String noticeType;

    /** 是否阅读 */
    private String isRead;

    /** 创建时间 */
    private Date createTime;

    /** 阅读时间 */
    private Date readTime;


}
