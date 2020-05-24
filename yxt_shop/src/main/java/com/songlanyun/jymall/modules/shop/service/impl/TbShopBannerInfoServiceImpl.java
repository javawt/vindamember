package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.dao.TbShopBannerInfoDao;
import com.songlanyun.jymall.modules.shop.entity.TbShopBannerInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopBannerInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺轮播图表(TbShopBannerInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-13 14:05:44
 */
@Service("tbShopBannerInfoService")
public class TbShopBannerInfoServiceImpl implements TbShopBannerInfoService {
    @Resource
    private TbShopBannerInfoDao tbShopBannerInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbShopBannerInfo queryById(Long id) {
        return this.tbShopBannerInfoDao.queryById(id);
    }

    @Override
    public R selectList(TbShopBannerInfo tbShopBannerInfo) {
        R r =new R();
        if(tbShopBannerInfo.getPage()>0){
        }else{
            return r.error(400,"page页码参数输入错误");
        }
        if(tbShopBannerInfo.getSize()>=0){
        }else{
            return r.error(400,"size页数参数输入错误");
        }
        List<TbShopBannerInfo> resultList = tbShopBannerInfoDao.selectList(tbShopBannerInfo);
        if(null!=resultList && resultList.size()>0){
            r.put("total", resultList.size());
            r.put("data", resultList.stream().skip((tbShopBannerInfo.getPage() - 1) * tbShopBannerInfo.getSize()).
                    limit(tbShopBannerInfo.getSize()).collect(Collectors.toList()));
            r.put("totalPages", (resultList.size()-1)/tbShopBannerInfo.getSize()+1);
        }else{
            r.put("total", 0);
            r.put("data",  new ArrayList<>());
            r.put("totalPages", 0);
        }
        return r;
    }


    /**
     * 新增数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 实例对象
     */
    @Override
    public R insert(TbShopBannerInfo tbShopBannerInfo) {
        R r = new R();
        int num = this.tbShopBannerInfoDao.insert(tbShopBannerInfo);
        if(num>0){
            return r;
        }else{
            return r.error(500,"新增失败");
        }
    }

    /**
     * 修改数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 实例对象
     */
    @Override
    public R update(TbShopBannerInfo tbShopBannerInfo) {
        R r =new R();
        int num = this.tbShopBannerInfoDao.update(tbShopBannerInfo);
        if(num>0){
            return r.put("data",this.queryById(tbShopBannerInfo.getId()));
        }else{
            return r.error(500,"修改数据失败");
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public R deleteById(Long id) {
        R r =new R();
        int num =this.tbShopBannerInfoDao.deleteById(id);
        if(num >0){
            return  r;
        }else{
            return r.error(500,"删除失败");
        }
    }
}