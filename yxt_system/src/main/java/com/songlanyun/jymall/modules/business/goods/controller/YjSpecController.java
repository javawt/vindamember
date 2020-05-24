package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.*;
import com.songlanyun.jymall.modules.business.goods.service.*;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


/**
 * 商品规格组记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("goods/yjspec")
public class YjSpecController {
    @Autowired
    private YjSpecService yjSpecService;

    @Autowired
    private YjSpecValueService yjSpecValueService;

    @Autowired
    private YjSpecRelService yjSpecRelService;

    @Autowired
    private YjGoodsService yjGoodsService;

    @Autowired
    private YjGoodsSkuService yjGoodsSkuService;
    @Resource
    private SysAdminDao sysAdminDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @Cacheable( value="sly:xlgshop:yjspecList",keyGenerator = "keyGenerator")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = yjSpecService.queryPage(params);
        List<YjSpec> list= (List<YjSpec>)page.getList();
        if(null!=list && list.size()>0){
            List resList=new ArrayList();
            for (YjSpec yjSpec:list){
                YjSpecRelValue yjSpecRelValue=new YjSpecRelValue();
                yjSpecRelValue.setSpecId(yjSpec.getSpecId());
                yjSpecRelValue.setWxappId(yjSpec.getWxappId());
                yjSpecRelValue.setSpecName(yjSpec.getSpecName());
                yjSpecRelValue.setCreateTime(yjSpec.getCreateTime());
                Map map=new HashMap();
                map.put("specId",yjSpec.getSpecId());
                List<YjSpecValue> resListP= yjSpecValueService.findAll(map);
                String sv="";
                if(null!=resListP && resListP.size()>0){
                    for(YjSpecValue yjSpecValue:resListP){
                        if(StringUtils.isNotBlank(sv)){
                            sv=sv+","+yjSpecValue.getSpecValue();
                        }else{
                            sv=sv+yjSpecValue.getSpecValue();
                        }
                    }
                }
                yjSpecRelValue.setSpecValue(sv);
                resList.add(yjSpecRelValue);
            }
            page.setList(resList);
        }

        return R.ok().put("page", page);
    }

    /**
     * sku属性列表,查询所有
     */
    @RequestMapping("/skuList")
    public R skuList() {
        List<YjSpec> list = yjSpecService.findAll();
        Set set=new HashSet();
        if (null != list && list.size() > 0) {
            for (YjSpec yjSpec : list) {
                if(!set.contains(yjSpec.getSpecId())){
                    Map map = new HashMap();
                    map.put("specId", yjSpec.getSpecId());
                    List<YjSpecValue> specValueList = yjSpecValueService.findAll(map);
                    if(null!=specValueList && specValueList.size()>0){
                        specValueList.forEach(v ->{
                            v.setSpecName(yjSpec.getSpecName());
                        });
                    }
                    yjSpec.setSpecValueList(specValueList);
                    set.add(yjSpec.getSpecId());
                }
            }
        }
        return R.ok().put("data", list);
    }

    /**
     * sku商品属性详情
     */
    @RequestMapping("/skuGoodsInfo/{goodsId}")
    public R skuGoodsInfo(@PathVariable("goodsId") Integer goodsId) {
        Map<String,Object> resMap=new HashMap();
        Map para=new HashMap();
        para.put("goodsId",goodsId);
        List<YjSpecRel> specRelList=yjSpecRelService.findAll(para);
        //resMap.put("goodsId",goodsId);
        resMap.put("goodsData",yjGoodsService.findById(goodsId));
        List specList=new ArrayList();
        if(null!=specRelList && specRelList.size()>0){
            Set set=new HashSet();
            for(YjSpecRel yjSpecRel:specRelList){
                if (!set.contains(yjSpecRel.getSpecId())) {
                    Map map = new HashMap();
                    map.put("specId", yjSpecRel.getSpecId());
                    map.put("specName", yjSpecService.findById(yjSpecRel.getSpecId()).getSpecName());
                    List list = new ArrayList();
                    for (YjSpecRel yjSpecRel1 : specRelList) {
                        if (yjSpecRel1.getSpecId() == yjSpecRel.getSpecId()) {
                            Map map1 = new HashMap();
                            map1.put("specValueId", yjSpecRel1.getSpecValueId());
                            map1.put("specValue", yjSpecValueService.findById(yjSpecRel1.getSpecValueId()).getSpecValue());
                            list.add(map1);
                        }
                    }
                    map.put("specValueList", list);
                    Map map2 = new HashMap();
                    map2.put("specId", yjSpecRel.getSpecId());
                    List<YjSpecValue> speVaList = yjSpecValueService.findAll(map2);
                    List list2 = new ArrayList();
                    if (null != speVaList && speVaList.size() > 0) {
                        for (YjSpecValue yjs : speVaList) {
                            Map map3 = new HashMap();
                            map3.put("specValueId", yjs.getSpecValueId());
                            map3.put("specValue", yjs.getSpecValue());
                            map3.put("specName",sysAdminDao.getSpecName(yjs.getSpecId()));
                            list2.add(map3);
                        }
                    }
                    map.put("specValueAllList", list2);
                    specList.add(map);
                    set.add(yjSpecRel.getSpecId());
                }
            }
        }
        resMap.put("specList",specList);
        return R.ok().put("data", resMap);
    }
    /**
     * sku属性保存
     */
    @RequestMapping("/skuSave")
    @Caching(evict = {@CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true),@CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)})
    public R skuSave(@RequestBody YjSkuRel yjSkuRel){
        //清空关系根据商品id
        yjSpecRelService.deleteYhSpecRelByGoodsId(yjSkuRel.getGoodsId());
        List<YjSkuRelSon> dataList=yjSkuRel.getDataList();
        if(null != dataList && dataList.size()>0){
            System.out.println(dataList.size());
            for(YjSkuRelSon yjSkuRelSon:dataList){
                List<Integer> specValueIdList=yjSkuRelSon.getSpecValueIdList();
                if(null !=specValueIdList && specValueIdList.size()>0){
                    for(Integer i:specValueIdList){
                        YjSpecRel yjSpecRel=new YjSpecRel();
                        yjSpecRel.setGoodsId(yjSkuRel.getGoodsId());
                        yjSpecRel.setSpecId(yjSkuRelSon.getSpecId());
                        yjSpecRel.setSpecValueId(i);
                        yjSpecRel.setCreateTime(new Date());
                        yjSpecRel.setWxappId(0);
                        yjSpecRelService.save(yjSpecRel);
                    }
                }
            }
        }
        return R.ok();
    }

    @RequestMapping("/batchSave")
    @Caching(evict = {@CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true),@CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)})
    @Transactional
    public R save(@RequestBody List<YjGoodsSku> yjGoodsSkuList) {
        if(null!=yjGoodsSkuList && yjGoodsSkuList.size()>0){
            //清空goodsku属性
            yjGoodsSkuService.deleteYjGoodsSkuByGoodsId(yjGoodsSkuList.get(0).getGoodsId());
            Set set=new HashSet();
            for(YjGoodsSku yjGoodsSku:yjGoodsSkuList){
                if(StringUtils.isNotBlank(yjGoodsSku.getGoodsNo())){
                    if(set.contains(yjGoodsSku.getGoodsNo())){
                        return R.error("商品编码已存在");
                    }else{
                        set.add(yjGoodsSku.getGoodsNo());
                    }
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
            }
            yjGoodsSkuService.saveAll(yjGoodsSkuList);
            return R.ok();
        }else{
            return R.error("数据不能为空");
        }

    }

    /** 保存某sku属性及其值  **/
    @RequestMapping("/batchUpdate")
    @CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)
    public R batchUpdate(@RequestBody Map<String, Object> params){
        PageUtils page = yjSpecService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{specId}")
    //@RequiresPermissions("goods:yjspec:info")
    public R info(@PathVariable("specId") Integer specId){
        YjSpec yjSpec = yjSpecService.findById(specId);

        return R.ok().put("yjSpec", yjSpec);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)
    public R save(@RequestBody YjSpecRelValue yjSpecRelValue){
        //参数校验
        if(StringUtils.isBlank(yjSpecRelValue.getSpecName())){
            return R.error("规格组名称不能为空");
        }
        Map maps=new HashMap();
        maps.put("specName",yjSpecRelValue.getSpecName());
        Long num=yjSpecService.count(maps);
        maps.put("specId",yjSpecRelValue.getSpecId());
        Long num1=yjSpecService.count(maps);
        if((num-num1)>0){
            return R.error("规格组名称不能重复");
        }
        YjSpec yjSpec = new YjSpec();
        yjSpec.setSpecName(yjSpecRelValue.getSpecName());
        yjSpec.setCreateTime(new Date());
        yjSpec.setWxappId(0);
        yjSpec.setSpecId(yjSpecRelValue.getSpecId());
        yjSpecService.save(yjSpec);
        String temp=yjSpecRelValue.getSpecValue().replaceAll("，",",");
        List<String> result = Arrays.asList(temp.split(","));
        if(null!=result && result.size()>0){
            yjSpecValueService.deleteYjSpecValueBySpecId(yjSpec.getSpecId());
            for(String s:result){
                //处理specValue
                YjSpecValue yjSpecValue = new YjSpecValue();
                yjSpecValue.setSpecId(yjSpec.getSpecId());
                yjSpecValue.setSpecValue(s);
                yjSpecValue.setCreateTime(new Date());
                yjSpecValue.setWxappId(0);
                yjSpecValueService.save(yjSpecValue);
            }
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)
    public R update(@RequestBody YjSpecRelValue yjSpecRelValue){
        YjSpec yjSpec=new YjSpec();
        ValidatorUtils.validateEntity(yjSpec);
        yjSpecService.save(yjSpec);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @CacheEvict(value="sly:xlgshop:yjspecList",allEntries=true)
    public R delete(@RequestBody Integer[] specIds){
        yjSpecService.deleteByIds(specIds);
        if(null!=specIds && specIds.length>0){
            for(int i=0;i<specIds.length;i++){
                yjSpecValueService.deleteYjSpecValueBySpecId(specIds[i]);
            }
        }

        return R.ok();
    }

}
