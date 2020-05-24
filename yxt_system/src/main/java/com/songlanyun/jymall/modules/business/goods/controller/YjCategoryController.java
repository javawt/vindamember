package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.common.utils.tree.TreeModel;
import com.songlanyun.jymall.common.utils.tree.TreeUtil;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjCategory;
import com.songlanyun.jymall.modules.business.goods.service.YjCategoryService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("/goods/yjcategory")
public class YjCategoryController {
    @Autowired
    private YjCategoryService yjCategoryService;
    @Resource
    private SysAdminDao sysAdminDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("goods:yjcategory:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yjCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{categoryId}")
    //@RequiresPermissions("goods:yjcategory:info")
    public R info(@PathVariable("categoryId") Integer categoryId) {
        YjCategory yjCategory = yjCategoryService.findById(categoryId);

        return R.ok().put("yjCategory", yjCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:yjcategory:save")
    @CacheEvict(value="sly:xlgshop:yjcategoryTree",allEntries=true)
    public R save(@RequestBody YjCategory yjCategory) {
        if (yjCategory.getCategoryId() != null) {
            yjCategory.setUpdateId(ShiroUtils.getUserId());
            //商品分类，顶级分类下架后，下级分类状态应也同步变更为下架
            if(yjCategory.getCategoryStatus()==20){
                List<Integer> parList=new ArrayList<>();
                List<Integer> categoryIdList = sysAdminDao.getCategorySubId(yjCategory.getCategoryId());
                parList=getAllCategory(categoryIdList,parList);
                if(null!=parList && parList.size()>0){
                    for(Integer integer:parList){
                        sysAdminDao.updateCategoryStatus(integer);
                    }
                }
            }


        }

        if (yjCategory.getCreateTime() == null) {
            yjCategory.setCreateTime(new Date());
        }

        yjCategory.setUpdateTime(new Date());
        yjCategory.setUpdateId(ShiroUtils.getUserId());

        yjCategoryService.save(yjCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @CacheEvict(value="sly:xlgshop:yjcategoryTree",allEntries=true)
    //@RequiresPermissions("goods:yjcategory:update")
    public R update(@RequestBody YjCategory yjCategory) {
        ValidatorUtils.validateEntity(yjCategory);
        yjCategoryService.save(yjCategory);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @CacheEvict(value="sly:xlgshop:yjcategoryTree",allEntries=true)
    //@RequiresPermissions("goods:yjcategory:delete")
    public R delete(@PathVariable("id") Integer id) {
        if(null ==id){
            return R.error("参数不能为空");
        }
        //判断如果该分类下有商品则不让删除
        List<Integer> parList=new ArrayList<>();
        parList.add(id);
        List<Integer> categoryIdList = sysAdminDao.getCategorySubId(id);
        parList=getAllCategory(categoryIdList,parList);
        int num = sysAdminDao.getSubGoods(parList);
        if(num>0){
            return R.error("该分类下有商品信息，无法删除");
        }
        yjCategoryService.deleteById(id);

        return R.ok();
    }

    //递归查询所有子菜单
    public List<Integer> getAllCategory(List<Integer> categoryIdList,List<Integer> parList){
        if(null!=categoryIdList && categoryIdList.size()>0){
            for (Integer integer:categoryIdList){
                if(!parList.contains(integer)){
                    parList.add(integer);
                }
                parList=getAllCategory(sysAdminDao.getCategorySubId(integer),parList);
            }
        }
        return parList;
    }

    /**
     * 树形结构数据
     */
    @GetMapping("/tree")
    @Cacheable( value="sly:xlgshop:yjcategoryTree",keyGenerator = "keyGenerator")
    public R tree() {
        //List<YjCategory> list = yjCategoryService.findAll();
        List<YjCategory> list = sysAdminDao.getYjCategory();

        List<TreeModel<YjCategory>> trees = new ArrayList<>();
        list.forEach(l -> {
            TreeModel<YjCategory> tree = new TreeModel<>();
            tree.setId(l.getCategoryId() + "");
            tree.setName(l.getName());
            tree.setParentId(l.getParentId() + "");
            tree.setParentKey(l.getCategoryId() + "");
            tree.setDisabled(false);
            tree.setData(l);
            trees.add(tree);
        });
        List<TreeModel<TreeModel<YjCategory>>> treeModels = TreeUtil.loadTree(trees, "0");

        return R.ok().put("data", treeModels);
    }


}
