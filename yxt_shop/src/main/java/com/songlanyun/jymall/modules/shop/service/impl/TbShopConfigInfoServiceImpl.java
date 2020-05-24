package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.dao.TbShopConfigInfoDao;
import com.songlanyun.jymall.modules.shop.entity.TbShopConfigInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopConfigInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺配置信息表(TbShopConfigInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-13 10:16:05
 */
@Service("tbShopConfigInfoService")
public class TbShopConfigInfoServiceImpl implements TbShopConfigInfoService {
    @Resource
    private TbShopConfigInfoDao tbShopConfigInfoDao;

    /**
     * 修改数据
     *
     * @param tbShopConfigInfo 实例对象
     * @return 实例对象
     */
    @Override
    public R update(TbShopConfigInfo tbShopConfigInfo) {
        R r =new R();
        this.tbShopConfigInfoDao.update(tbShopConfigInfo);
        return r.put("data",tbShopConfigInfoDao.getOne(tbShopConfigInfo.getId()));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public R deleteById(Long id) {
        R r = new R();
        int num=this.tbShopConfigInfoDao.deleteById(id);
        if(num > 0){
            return r;
        }else{
            return r.error(500,"删除失败,记录不存在！");
        }
    }

    @Override
    public R save(TbShopConfigInfo tbShopConfigInfo) {
        R r = new R();
        try{
            this.tbShopConfigInfoDao.save(tbShopConfigInfo);
            return r;
        }catch (Exception e){
            return r.error(500,"保存失败");
        }

    }

    @Override
    public R select(TbShopConfigInfo tbShopConfigInfo) {
        R r =new R();
        if(tbShopConfigInfo.getPage()>0){
        }else{
            return r.error(400,"page页码参数输入错误");
        }
        if(tbShopConfigInfo.getSize()>=0){
        }else{
            return r.error(400,"size页数参数输入错误");
        }
        List<TbShopConfigInfo> resultList = tbShopConfigInfoDao.select(tbShopConfigInfo);
        if(null!=resultList && resultList.size()>0){
            r.put("total", resultList.size());
            r.put("data", resultList.stream().skip((tbShopConfigInfo.getPage() - 1) * tbShopConfigInfo.getSize()).
                    limit(tbShopConfigInfo.getSize()).collect(Collectors.toList()));
            r.put("totalPages", (resultList.size()-1)/tbShopConfigInfo.getSize()+1);
        }else{
            r.put("total", 0);
            r.put("data",  new ArrayList<>());
            r.put("totalPages", 0);
        }
        return r;
    }
}