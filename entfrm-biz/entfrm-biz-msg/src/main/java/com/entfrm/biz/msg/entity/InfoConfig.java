package com.entfrm.biz.msg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.core.base.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.entfrm.core.data.entity.BaseEntity;

/**
 * @author entfrm
 * @date 2020-05-10 14:53:43
 * @description 消息配置对象 InfoConfig
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_info_config")
public class InfoConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    private Integer id;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private String type;

    /**
     * key编号
     */
    private String accessKeyId;

    /**
     * 密钥
     */
    private String accessKeySecret;

    /**
     * 签名
     */
    private String signName;

    /**
     * 模板code
     */
    @Excel(name = "模板code")
    private String templateCode;

    /**
     * 模板参数
     */
    private String templateParam;

    /**
     * 地址
     */
    @Excel(name = "地址")
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 发送人
     */
    private String fromBy;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String pass;

    /**
     * 其他信息
     */
    private String otherInfo;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", convertExp = "0=正常,1=停用")
    private String status;


}
