package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopConfigInfo;
import com.songlanyun.jymall.modules.shop.entity.TbShopServerMechanicInfo;
import com.songlanyun.jymall.modules.shop.dao.TbShopServerMechanicInfoDao;
import com.songlanyun.jymall.modules.shop.service.TbShopServerMechanicInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺服务技工信息表(TbShopServerMechanicInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-22 10:19:35
 */
@Service("tbShopServerMechanicInfoService")
public class TbShopServerMechanicInfoServiceImpl implements TbShopServerMechanicInfoService {
    @Resource
    private TbShopServerMechanicInfoDao tbShopServerMechanicInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbShopServerMechanicInfo queryById(Long id) {
        return this.tbShopServerMechanicInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<TbShopServerMechanicInfo> queryAllByLimit(int offset, int limit) {
        return this.tbShopServerMechanicInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TbShopServerMechanicInfo insert(TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        this.tbShopServerMechanicInfoDao.insert(tbShopServerMechanicInfo);
        return tbShopServerMechanicInfo;
    }

    /**
     * 修改数据
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 实例对象
     */
    @Override
    public R update(TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        R r =new R();
        this.tbShopServerMechanicInfoDao.update(tbShopServerMechanicInfo);
        return r.put("data",this.queryById(tbShopServerMechanicInfo.getId()));
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
        int num=this.tbShopServerMechanicInfoDao.deleteById(id);
        if(num > 0){
            return r;
        }else{
            return r.error(500,"删除失败,记录不存在！");
        }
    }

    @Override
    public R select(TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        R r =new R();
        if(tbShopServerMechanicInfo.getPage()>0){
        }else{
            return r.error(400,"page页码参数输入错误");
        }
        if(tbShopServerMechanicInfo.getSize()>=0){
        }else{
            return r.error(400,"size页数参数输入错误");
        }
        List<TbShopServerMechanicInfo> resultList = tbShopServerMechanicInfoDao.select(tbShopServerMechanicInfo);
        if(null!=resultList && resultList.size()>0){
            r.put("total", resultList.size());
            r.put("data", resultList.stream().skip((tbShopServerMechanicInfo.getPage() - 1) * tbShopServerMechanicInfo.getSize()).
                    limit(tbShopServerMechanicInfo.getSize()).collect(Collectors.toList()));
            r.put("totalPages", (resultList.size()-1)/tbShopServerMechanicInfo.getSize()+1);
        }else{
            r.put("total", 0);
            r.put("data",  new ArrayList<>());
            r.put("totalPages", 0);
        }
        return r;
    }

    @Override
    public R save(TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        R r = new R();
        try{
            this.tbShopServerMechanicInfoDao.insert(tbShopServerMechanicInfo);
            return r;
        }catch (Exception e){
            return r.error(500,"保存失败");
        }
    }
}