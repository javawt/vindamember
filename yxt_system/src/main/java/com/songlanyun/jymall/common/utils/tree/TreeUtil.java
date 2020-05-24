package com.songlanyun.jymall.common.utils.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形数据加载工具类
 * Created by zenghang on 2019/7/31.
 */
public class TreeUtil {

    /**
     * 加载整树,默认parentId为0
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends TreeBaseBean> List<TreeModel<T>> loadTree(List<T> data) {
        List<TreeModel<T>> treeModels = new ArrayList<>();
        data.stream().filter((d) -> d.getParentId().equals("0")).collect(Collectors.toList()).forEach(l -> {
            TreeModel<T> treeModel = new TreeModel<>();
            treeModel.setId(l.getId());
            treeModel.setName(l.getName());
            treeModel.setParentId(l.getParentId());
            treeModel.setParentKey(l.getParentKey());
            treeModel.setDisabled(l.getDisabled());
            treeModel.setData(l);
            treeModel.setChildren(loadTree(data, l.getParentKey()));
            treeModels.add(treeModel);
        });

        return treeModels;
    }

    /**
     * 指定parentId加载部分树
     *
     * @param data
     * @param parentId
     * @param <T>
     * @return
     */
    public static <T extends TreeBaseBean> List<TreeModel<T>> loadTree(List<T> data, String parentId) {
        List<TreeModel<T>> treeModels = new ArrayList<>();
        data.stream().filter((d) -> d.getParentId().equals(parentId)).collect(Collectors.toList()).forEach(l -> {
            TreeModel<T> treeModel = new TreeModel<>();
            treeModel.setId(l.getId());
            treeModel.setName(l.getName());
            treeModel.setParentId(l.getParentId());
            treeModel.setParentKey(l.getParentKey());
            treeModel.setDisabled(l.getDisabled());
            treeModel.setData(l);
            treeModel.setChildren(loadTree(data, l.getParentKey()));
            treeModels.add(treeModel);
        });
        return treeModels;
    }

    /**
     * 指定parentKey加载指定节点的树和子级数据
     *
     * @param data
     * @param parentKey
     * @param <T>
     * @return
     */
    public static <T extends TreeBaseBean> List<TreeModel<T>> loadPartTree(List<T> data, String parentKey) {
        List<TreeModel<T>> treeModels = new ArrayList<>();
        data.stream().filter((d) -> d.getParentKey().equals(parentKey)).collect(Collectors.toList()).forEach(l -> {
            TreeModel<T> treeModel = new TreeModel<>();
            treeModel.setId(l.getId());
            treeModel.setName(l.getName());
            treeModel.setParentId(l.getParentId());
            treeModel.setParentKey(l.getParentKey());
            treeModel.setDisabled(l.getDisabled());
            treeModel.setData(l);
            treeModel.setChildren(loadTree(data, l.getParentKey()));
            treeModels.add(treeModel);
        });
        return treeModels;
    }

    public static void main(String[] args) {
        List<TreeBaseBean> list = new ArrayList<>();

        //设置一级树
        for (int i = 1; i <= 10; i++) {
            TreeBaseBean treeBaseBean = new TreeBaseBean();
            treeBaseBean.setId(String.valueOf(i));
            treeBaseBean.setName("一级目录:" + i);
            treeBaseBean.setParentId("0");
            treeBaseBean.setParentKey("1-" + i);
            list.add(treeBaseBean);
        }

        //设置二级树
        for (int i = 0; i < 50; i++) {
            TreeBaseBean treeBaseBean = new TreeBaseBean();
            treeBaseBean.setId(String.valueOf(i));
            treeBaseBean.setName("二级目录:" + i);
            String parentId = String.valueOf((int) (1 + Math.random() * 10));
            treeBaseBean.setParentId("1-" + parentId);
            treeBaseBean.setParentKey("2-" + i);
            list.add(treeBaseBean);
        }

        //设置三级树
        for (int i = 0; i < 100; i++) {
            TreeBaseBean treeBaseBean = new TreeBaseBean();
            treeBaseBean.setId(String.valueOf(i));
            treeBaseBean.setName("三级目录:" + i);
            String parentId = "2-" + ((int) (Math.random() * 50));
            treeBaseBean.setParentId(parentId);
            treeBaseBean.setParentKey("3-" + i);
            list.add(treeBaseBean);
        }

        //加载树
        TreeUtil treeUtil = new TreeUtil();

        //加载整树
        System.out.println();
        System.out.println("-------------------------  加载整树  -------------------------");
        System.out.println();
        List<TreeModel<TreeBaseBean>> allTree = loadTree(list);
        treeUtil.printTree(allTree, 0);

        //指定parentId加载部分树
        System.out.println();
        System.out.println("-------------------------  指定parentId加载部分树  -------------------------");
        System.out.println();
        List<TreeModel<TreeBaseBean>> partTree = loadTree(list, "1-1");
        treeUtil.printTree(partTree, 0);

        //指定parentKey加载指定节点树
        System.out.println();
        System.out.println("-------------------------  指定parentKey加载指定节点树  -------------------------");
        System.out.println();
        List<TreeModel<TreeBaseBean>> partTree2 = loadPartTree(list, "1-2");
        treeUtil.printTree(partTree2, 0);
    }

    /**
     * 输出树形结构
     *
     * @param tree
     * @param level
     */
    public void printTree(List<TreeModel<TreeBaseBean>> tree, int level) {
        for (TreeModel<TreeBaseBean> treeBaseBeanTreeModel : tree) {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.print(treeBaseBeanTreeModel.getName());
//            System.out.print("name->" + treeBaseBeanTreeModel.getName());
//            System.out.print("\t");
//            System.out.print("parentId->" + treeBaseBeanTreeModel.getParentId());
//            System.out.print("\t");
//            System.out.print("parentKey->" + treeBaseBeanTreeModel.getParentKey());
            System.out.println();
            if (treeBaseBeanTreeModel.getChildren().size() > 0) {
                printTree(treeBaseBeanTreeModel.getChildren(), level + 1);
            }
        }

    }
}
