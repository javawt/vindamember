package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsSkuService;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecValueService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


/**
 * 商品规格表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("goods/yjgoodssku")
public class YjGoodsSkuController {
    @Autowired
    private YjGoodsSkuService yjGoodsSkuService;

    @Autowired
    private YjSpecValueService yjSpecValueService;

    @Resource
    private SysAdminDao sysAdminDao;
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("goods:yjgoodssku:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yjGoodsSkuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据商品id 得到其对应 sku
     **/
    @RequestMapping("/getGoodsSku/{goodsId}")
    public R getGoodsSku(@PathVariable("goodsId") int goodsId) {
        HashMap param = new HashMap();
        param.put("goodsId", goodsId);
        List<YjGoodsSku> resList = yjGoodsSkuService.findAll(param);
        if (null != resList && resList.size() > 0) {
            for (YjGoodsSku yjGoodsSku : resList) {
                //新增specName;
                yjGoodsSku.setSpecName(sysAdminDao.getGoodSpecName(goodsId));
                String temp ="";
                if(null!=yjGoodsSku){
                    if (StringUtils.isNotBlank(yjGoodsSku.getSpecSkuId())) {
                        temp = yjGoodsSku.getSpecSkuId().replaceAll("，", ",");
                    }}
                if (StringUtils.isNotBlank(temp)) {
                    String vl = "";
                    List<String> result = Arrays.asList(temp.split(","));
                    if (null != result && result.size() > 0) {
                        for (String s : result) {
                            if (StringUtils.isNotBlank(vl)) {
                                vl = vl + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                            } else {
                                vl = vl + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                            }
                        }
                    }
                    yjGoodsSku.setSpecValue(vl);
                }
            }
        }
        return R.ok().put("yjGoodsSku", resList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{goodsSkuId}")
    //@RequiresPermissions("goods:yjgoodssku:info")
    public R info(@PathVariable("goodsSkuId") Integer goodsSkuId) {
        YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(goodsSkuId);

        return R.ok().put("yjGoodsSku", yjGoodsSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:yjgoodssku:save")
    public R save(@RequestBody YjGoodsSku yjGoodsSku) {
        //判断sku的goodno是否重复
        if(StringUtils.isNotBlank(yjGoodsSku.getGoodsNo())){
            int num = sysAdminDao.getGoodsNo(yjGoodsSku);
            if(num>0){
                return R.error("商品编码已存在");
            }
        }

        if (yjGoodsSku.getGoodsSkuId() == null) {
            yjGoodsSku.setCreateTime(new Date());
        } else {
            yjGoodsSku.setUpdateTime(new Date());
        }
        if(StringUtils.isBlank(yjGoodsSku.getSpecSkuId())){
            yjGoodsSku.setSpecSkuId("");
        }
        yjGoodsSkuService.save(yjGoodsSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("goods:yjgoodssku:update")
    public R update(@RequestBody YjGoodsSku yjGoodsSku) {
        ValidatorUtils.validateEntity(yjGoodsSku);
        yjGoodsSkuService.save(yjGoodsSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("goods:yjgoodssku:delete")
    public R delete(@RequestBody Integer[] goodsSkuIds) {
        yjGoodsSkuService.deleteByIds(goodsSkuIds);

        return R.ok();
    }

}
