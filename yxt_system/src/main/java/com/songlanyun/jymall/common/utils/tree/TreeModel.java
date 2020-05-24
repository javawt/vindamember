package com.songlanyun.jymall.common.utils.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 返回树形结构数据的模型类
 * Created by zenghang on 2019/7/31.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TreeModel<T> extends TreeBaseBean implements Serializable {
    private List<TreeModel<T>> children;
    private T data;
}
