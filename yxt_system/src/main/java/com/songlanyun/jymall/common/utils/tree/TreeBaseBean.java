package com.songlanyun.jymall.common.utils.tree;

import lombok.Data;

import java.io.Serializable;

/**
 * 该类为树形结构的表基础字段模型
 * Created by zenghang on 2019/7/31.
 */
@Data
public class TreeBaseBean implements Serializable {
    /**
     * 主键ID，应为唯一，可以作为子级的parentId
     */
    private String id;
    /**
     * 基础字段
     */
    private String name;
    /**
     * 父级的特征值，可以为id，也可以为parentKey
     */
    private String parentId;
    /**
     * 特征值，如果使用id作为parentId该字段和id相同即可
     * 该字段的意义在于同时存在多个父级的情况
     */
    private String parentKey;

    //element tree 属性
    /**
     * 指定节点选择框是否禁用为节点对象的某个属性值
     */
    private Boolean disabled;
}
